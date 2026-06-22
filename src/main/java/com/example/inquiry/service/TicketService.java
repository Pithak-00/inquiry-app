package com.example.inquiry.service;

import com.example.inquiry.dto.TicketDetailDto;
import com.example.inquiry.dto.TicketForm;
import com.example.inquiry.dto.TicketSearchForm;
import com.example.inquiry.entity.*;
import com.example.inquiry.entity.Ticket.TicketStatus;
import com.example.inquiry.exception.ResourceNotFoundException;
import com.example.inquiry.repository.*;
import com.example.inquiry.validator.TicketStatusTransitionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final TicketStatusLogRepository statusLogRepository;
    private final TicketStatusTransitionValidator transitionValidator;
    private final AttachmentService attachmentService;

    @Value("${app.ticket.page-size:20}")
    private int pageSize;

    public TicketService(
        TicketRepository ticketRepository,
        CategoryRepository categoryRepository,
        TicketStatusLogRepository statusLogRepository,
        TicketStatusTransitionValidator transitionValidator,
        AttachmentService attachmentService
    ) {
        this.ticketRepository    = ticketRepository;
        this.categoryRepository  = categoryRepository;
        this.statusLogRepository = statusLogRepository;
        this.transitionValidator = transitionValidator;
        this.attachmentService   = attachmentService;
    }

    @Transactional
    public Ticket create(TicketForm form, User owner) {
        Category category = resolveCategory(form.getCategoryId());

        Ticket ticket = new Ticket();
        ticket.setUser(owner);
        ticket.setTitle(form.getTitle());
        ticket.setBody(form.getBody());
        ticket.setStatus(TicketStatus.NEW);
        ticket.setPriority(form.getPriority());
        ticket.setCategory(category);

        Ticket saved = ticketRepository.save(ticket);
        log.info("チケットを作成しました: id={}, userId={}", saved.getId(), owner.getId());

        // 添付ファイルアップロード
        if (form.getAttachments() != null && !form.getAttachments().isEmpty()) {
            attachmentService.upload(form.getAttachments(), saved);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public TicketDetailDto findDetailById(Long id, Long loginUserId, boolean isAdmin) {
        Ticket ticket = getTicketOrThrow(id);

        // 所有者チェック（管理者はすべてアクセス可能）
        if (!isAdmin && !ticket.getUser().getId().equals(loginUserId)) {
            throw new AccessDeniedException("このチケットにアクセスする権限がありません");
        }

        // 編集可能かどうか
        boolean editable = ticket.getUser().getId().equals(loginUserId)
            && (ticket.getStatus() == TicketStatus.NEW || ticket.getStatus() == TicketStatus.IN_PROGRESS);

        return TicketDetailDto.builder()
            .id(ticket.getId())
            .title(ticket.getTitle())
            .body(ticket.getBody())
            .status(ticket.getStatus())
            .priority(ticket.getPriority())
            .categoryName(ticket.getCategory() != null ? ticket.getCategory().getName() : null)
            .ownerId(ticket.getUser().getId())
            .ownerName(ticket.getUser().getName())
            .createdAt(ticket.getCreatedAt())
            .updatedAt(ticket.getUpdatedAt())
            .comments(ticket.getComments())
            .attachments(ticket.getAttachments())
            .statusLogs(ticket.getStatusLogs())
            .editable(editable)
            .build();
    }

    @Transactional(readOnly = true)
    public Page<Ticket> search(TicketSearchForm form, Long loginUserId, boolean isAdmin) {
        Pageable pageable = PageRequest.of(form.getPage(), pageSize);
        LocalDateTime dateFrom = form.getDateFrom() != null ? form.getDateFrom().atStartOfDay() : null;
        LocalDateTime dateTo   = form.getDateTo()   != null ? form.getDateTo().plusDays(1).atStartOfDay() : null;

        // LIKE句のためにワイルドカードを付与（nullの場合はnullのまま）
        String keyword = (form.getKeyword() != null && !form.getKeyword().isBlank())
            ? "%" + form.getKeyword() + "%"
            : null;

        if (isAdmin) {
            return ticketRepository.searchAll(
                keyword, form.getStatus(), form.getPriority(), form.getCategoryId(), dateFrom, dateTo, pageable
            );
        } else {
            return ticketRepository.searchByUser(
                loginUserId, keyword, form.getStatus(), form.getPriority(), form.getCategoryId(), dateFrom, dateTo, pageable
            );
        }
    }

    @Transactional
    public Ticket update(Long id, TicketForm form, Long loginUserId) {
        Ticket ticket = getTicketOrThrow(id);

        // 編集権限チェック
        if (!ticket.getUser().getId().equals(loginUserId)) {
            throw new AccessDeniedException("このチケットを編集する権限がありません");
        }
        if (ticket.getStatus() != TicketStatus.NEW && ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new AccessDeniedException("このステータスのチケットは編集できません");
        }

        Category category = resolveCategory(form.getCategoryId());

        ticket.setTitle(form.getTitle());
        ticket.setBody(form.getBody());
        ticket.setPriority(form.getPriority());
        ticket.setCategory(category);

        Ticket saved = ticketRepository.save(ticket);

        // 追加添付ファイル
        if (form.getAttachments() != null && !form.getAttachments().isEmpty()) {
            attachmentService.upload(form.getAttachments(), saved);
        }

        log.info("チケットを更新しました: id={}", id);
        return saved;
    }

    @Transactional
    public void changeStatus(Long ticketId, TicketStatus toStatus, User changedBy) {
        Ticket ticket = getTicketOrThrow(ticketId);

        transitionValidator.validate(ticket.getStatus(), toStatus);
        TicketStatus fromStatus = ticket.getStatus();

        ticket.setStatus(toStatus);
        ticketRepository.save(ticket);

        TicketStatusLog statusLog = new TicketStatusLog();
        statusLog.setTicket(ticket);
        statusLog.setChangedBy(changedBy);
        statusLog.setFromStatus(fromStatus);
        statusLog.setToStatus(toStatus);
        statusLogRepository.save(statusLog);

        log.info("ステータスを変更しました: ticketId={}, {} -> {}", ticketId, fromStatus, toStatus);
    }

    @Transactional
    public void softDelete(Long ticketId, User deletedBy) {
        Ticket ticket = getTicketOrThrow(ticketId);
        ticket.setDeletedAt(LocalDateTime.now());
        ticketRepository.save(ticket);
        log.info("チケットを論理削除しました: id={}, deletedBy={}", ticketId, deletedBy.getId());
    }

    @Transactional(readOnly = true)
    public Ticket getTicketOrThrow(Long id) {
        Ticket ticket = ticketRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("チケットが見つかりません: " + id));
        if (ticket.isDeleted()) {
            throw new ResourceNotFoundException("このチケットは削除されています: " + id);
        }
        return ticket;
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId).orElse(null);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }
}

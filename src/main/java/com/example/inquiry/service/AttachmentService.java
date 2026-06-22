package com.example.inquiry.service;

import com.example.inquiry.entity.Ticket;
import com.example.inquiry.entity.TicketAttachment;
import com.example.inquiry.exception.InvalidFileTypeException;
import com.example.inquiry.exception.ResourceNotFoundException;
import com.example.inquiry.repository.TicketAttachmentRepository;
import com.example.inquiry.util.FileStorageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AttachmentService {

    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);

    private final TicketAttachmentRepository attachmentRepository;
    private final FileStorageUtil fileStorageUtil;

    @Value("${app.upload.max-files-per-ticket}")
    private int maxFilesPerTicket;

    public AttachmentService(
        TicketAttachmentRepository attachmentRepository,
        FileStorageUtil fileStorageUtil
    ) {
        this.attachmentRepository = attachmentRepository;
        this.fileStorageUtil      = fileStorageUtil;
    }

    @Transactional
    public void upload(List<MultipartFile> files, Ticket ticket) {
        if (files == null || files.isEmpty()) return;

        long existingCount = attachmentRepository.countByTicketId(ticket.getId());

        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) continue;

            if (existingCount >= maxFilesPerTicket) {
                throw new InvalidFileTypeException(
                    "添付ファイルは1チケットにつき最大" + maxFilesPerTicket + "枚です"
                );
            }

            try {
                String mimeType   = fileStorageUtil.detectMimeType(file);
                String storedPath = fileStorageUtil.store(file, ticket.getId());

                TicketAttachment attachment = new TicketAttachment();
                attachment.setTicket(ticket);
                attachment.setOriginalName(file.getOriginalFilename());
                attachment.setPath(storedPath);
                attachment.setMimeType(mimeType);
                attachment.setSize(file.getSize());

                attachmentRepository.save(attachment);
                existingCount++;
                log.info("添付ファイルを保存しました: ticketId={}, file={}", ticket.getId(), file.getOriginalFilename());

            } catch (IOException e) {
                log.error("添付ファイルの保存に失敗しました: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("ファイルの保存に失敗しました: " + file.getOriginalFilename(), e);
            }
        }
    }

    @Transactional(readOnly = true)
    public TicketAttachment getFile(Long attachmentId, Long loginUserId, boolean isAdmin) {
        TicketAttachment attachment = attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new ResourceNotFoundException("添付ファイルが見つかりません: " + attachmentId));

        if (!isAdmin && !attachment.getTicket().getUser().getId().equals(loginUserId)) {
            throw new AccessDeniedException("このファイルにアクセスする権限がありません");
        }

        return attachment;
    }

    @Transactional
    public void deleteByTicket(Long ticketId) {
        List<TicketAttachment> attachments = attachmentRepository.findByTicketId(ticketId);
        for (TicketAttachment attachment : attachments) {
            fileStorageUtil.delete(attachment.getPath());
        }
        attachmentRepository.deleteAll(attachments);
    }
}

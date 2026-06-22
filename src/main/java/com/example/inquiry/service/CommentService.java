package com.example.inquiry.service;

import com.example.inquiry.dto.CommentForm;
import com.example.inquiry.entity.Ticket;
import com.example.inquiry.entity.TicketComment;
import com.example.inquiry.entity.User;
import com.example.inquiry.repository.TicketCommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final TicketCommentRepository commentRepository;

    public CommentService(TicketCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public TicketComment addComment(Ticket ticket, User user, CommentForm form, boolean isAdmin) {
        // 一般ユーザーは自分のチケットにのみコメント可能
        if (!isAdmin && !ticket.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("このチケットにコメントする権限がありません");
        }

        TicketComment comment = new TicketComment();
        comment.setTicket(ticket);
        comment.setUser(user);
        comment.setBody(form.getBody());

        TicketComment saved = commentRepository.save(comment);
        log.info("コメントを追加しました: ticketId={}, userId={}", ticket.getId(), user.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<TicketComment> findByTicketId(Long ticketId) {
        return commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId);
    }
}

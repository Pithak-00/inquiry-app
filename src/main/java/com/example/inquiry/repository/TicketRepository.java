package com.example.inquiry.repository;

import com.example.inquiry.entity.Ticket;
import com.example.inquiry.entity.Ticket.TicketStatus;
import com.example.inquiry.entity.Ticket.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // 管理者用：全チケット検索
    @Query("""
        SELECT t FROM Ticket t
        WHERE t.deletedAt IS NULL
          AND (:keyword IS NULL OR t.title LIKE :keyword OR t.body LIKE :keyword)
          AND (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:categoryId IS NULL OR t.category.id = :categoryId)
          AND (:dateFrom IS NULL OR t.createdAt >= :dateFrom)
          AND (:dateTo IS NULL OR t.createdAt < :dateTo)
        ORDER BY t.createdAt DESC
        """)
    Page<Ticket> searchAll(
        @Param("keyword") String keyword,
        @Param("status") TicketStatus status,
        @Param("priority") Priority priority,
        @Param("categoryId") Long categoryId,
        @Param("dateFrom") java.time.LocalDateTime dateFrom,
        @Param("dateTo") java.time.LocalDateTime dateTo,
        Pageable pageable
    );

    // 一般ユーザー用：自分のチケットのみ検索
    @Query("""
        SELECT t FROM Ticket t
        WHERE t.deletedAt IS NULL
          AND t.user.id = :userId
          AND (:keyword IS NULL OR t.title LIKE :keyword OR t.body LIKE :keyword)
          AND (:status IS NULL OR t.status = :status)
          AND (:priority IS NULL OR t.priority = :priority)
          AND (:categoryId IS NULL OR t.category.id = :categoryId)
          AND (:dateFrom IS NULL OR t.createdAt >= :dateFrom)
          AND (:dateTo IS NULL OR t.createdAt < :dateTo)
        ORDER BY t.createdAt DESC
        """)
    Page<Ticket> searchByUser(
        @Param("userId") Long userId,
        @Param("keyword") String keyword,
        @Param("status") TicketStatus status,
        @Param("priority") Priority priority,
        @Param("categoryId") Long categoryId,
        @Param("dateFrom") java.time.LocalDateTime dateFrom,
        @Param("dateTo") java.time.LocalDateTime dateTo,
        Pageable pageable
    );
}

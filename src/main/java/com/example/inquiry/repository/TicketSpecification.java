package com.example.inquiry.repository;

import com.example.inquiry.entity.Ticket;
import com.example.inquiry.entity.Ticket.Priority;
import com.example.inquiry.entity.Ticket.TicketStatus;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Ticket の検索条件を動的に組み立てる Specification クラス。
 * null が渡された条件は自動的に無視されます（PostgreSQL 互換）。
 */
public class TicketSpecification {

    private TicketSpecification() {}

    /** 論理削除されていないチケットのみ */
    public static Specification<Ticket> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    /** 特定ユーザーのチケットのみ */
    public static Specification<Ticket> byUser(Long userId) {
        return (root, query, cb) ->
            userId == null ? null : cb.equal(root.get("user").get("id"), userId);
    }

    /** タイトル・本文のキーワード検索 */
    public static Specification<Ticket> keywordLike(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        String pattern = "%" + keyword + "%";
        return (root, query, cb) ->
            cb.or(cb.like(root.get("title"), pattern),
                  cb.like(root.get("body"),  pattern));
    }

    /** ステータス絞り込み */
    public static Specification<Ticket> byStatus(TicketStatus status) {
        return (root, query, cb) ->
            status == null ? null : cb.equal(root.get("status"), status);
    }

    /** 優先度絞り込み */
    public static Specification<Ticket> byPriority(Priority priority) {
        return (root, query, cb) ->
            priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    /** カテゴリ絞り込み */
    public static Specification<Ticket> byCategoryId(Long categoryId) {
        return (root, query, cb) ->
            categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    /** 作成日（From）絞り込み */
    public static Specification<Ticket> createdAfter(LocalDateTime dateFrom) {
        return (root, query, cb) ->
            dateFrom == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom);
    }

    /** 作成日（To）絞り込み */
    public static Specification<Ticket> createdBefore(LocalDateTime dateTo) {
        return (root, query, cb) ->
            dateTo == null ? null : cb.lessThan(root.get("createdAt"), dateTo);
    }
}

package com.example.inquiry.dto;

import com.example.inquiry.entity.Ticket.Priority;
import com.example.inquiry.entity.Ticket.TicketStatus;
import java.time.LocalDateTime;

public class TicketListDto {

    private final Long id;
    private final String title;
    private final TicketStatus status;
    private final Priority priority;
    private final String categoryName;
    private final String ownerName;
    private final LocalDateTime createdAt;

    private TicketListDto(Builder b) {
        this.id           = b.id;
        this.title        = b.title;
        this.status       = b.status;
        this.priority     = b.priority;
        this.categoryName = b.categoryName;
        this.ownerName    = b.ownerName;
        this.createdAt    = b.createdAt;
    }

    public static Builder builder() { return new Builder(); }

    // Getters
    public Long getId()                 { return id; }
    public String getTitle()            { return title; }
    public TicketStatus getStatus()     { return status; }
    public Priority getPriority()       { return priority; }
    public String getCategoryName()     { return categoryName; }
    public String getOwnerName()        { return ownerName; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public static class Builder {
        private Long id;
        private String title;
        private TicketStatus status;
        private Priority priority;
        private String categoryName;
        private String ownerName;
        private LocalDateTime createdAt;

        public Builder id(Long id)                     { this.id = id; return this; }
        public Builder title(String title)             { this.title = title; return this; }
        public Builder status(TicketStatus status)     { this.status = status; return this; }
        public Builder priority(Priority priority)     { this.priority = priority; return this; }
        public Builder categoryName(String name)       { this.categoryName = name; return this; }
        public Builder ownerName(String name)          { this.ownerName = name; return this; }
        public Builder createdAt(LocalDateTime dt)     { this.createdAt = dt; return this; }

        public TicketListDto build() { return new TicketListDto(this); }
    }
}

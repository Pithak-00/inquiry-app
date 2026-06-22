package com.example.inquiry.dto;

import com.example.inquiry.entity.*;
import com.example.inquiry.entity.Ticket.Priority;
import com.example.inquiry.entity.Ticket.TicketStatus;
import java.time.LocalDateTime;
import java.util.List;

public class TicketDetailDto {

    private final Long id;
    private final String title;
    private final String body;
    private final TicketStatus status;
    private final Priority priority;
    private final String categoryName;
    private final Long ownerId;
    private final String ownerName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<TicketComment> comments;
    private final List<TicketAttachment> attachments;
    private final List<TicketStatusLog> statusLogs;
    private final boolean editable;

    private TicketDetailDto(Builder b) {
        this.id           = b.id;
        this.title        = b.title;
        this.body         = b.body;
        this.status       = b.status;
        this.priority     = b.priority;
        this.categoryName = b.categoryName;
        this.ownerId      = b.ownerId;
        this.ownerName    = b.ownerName;
        this.createdAt    = b.createdAt;
        this.updatedAt    = b.updatedAt;
        this.comments     = b.comments;
        this.attachments  = b.attachments;
        this.statusLogs   = b.statusLogs;
        this.editable     = b.editable;
    }

    public static Builder builder() { return new Builder(); }

    // Getters
    public Long getId()                          { return id; }
    public String getTitle()                     { return title; }
    public String getBody()                      { return body; }
    public TicketStatus getStatus()              { return status; }
    public Priority getPriority()                { return priority; }
    public String getCategoryName()              { return categoryName; }
    public Long getOwnerId()                     { return ownerId; }
    public String getOwnerName()                 { return ownerName; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public LocalDateTime getUpdatedAt()          { return updatedAt; }
    public List<TicketComment> getComments()     { return comments; }
    public List<TicketAttachment> getAttachments()   { return attachments; }
    public List<TicketStatusLog> getStatusLogs() { return statusLogs; }
    public boolean isEditable()                  { return editable; }

    public static class Builder {
        private Long id;
        private String title;
        private String body;
        private TicketStatus status;
        private Priority priority;
        private String categoryName;
        private Long ownerId;
        private String ownerName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<TicketComment> comments;
        private List<TicketAttachment> attachments;
        private List<TicketStatusLog> statusLogs;
        private boolean editable;

        public Builder id(Long id)                                     { this.id = id; return this; }
        public Builder title(String title)                             { this.title = title; return this; }
        public Builder body(String body)                               { this.body = body; return this; }
        public Builder status(TicketStatus status)                     { this.status = status; return this; }
        public Builder priority(Priority priority)                     { this.priority = priority; return this; }
        public Builder categoryName(String categoryName)               { this.categoryName = categoryName; return this; }
        public Builder ownerId(Long ownerId)                           { this.ownerId = ownerId; return this; }
        public Builder ownerName(String ownerName)                     { this.ownerName = ownerName; return this; }
        public Builder createdAt(LocalDateTime createdAt)              { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt)              { this.updatedAt = updatedAt; return this; }
        public Builder comments(List<TicketComment> comments)          { this.comments = comments; return this; }
        public Builder attachments(List<TicketAttachment> attachments) { this.attachments = attachments; return this; }
        public Builder statusLogs(List<TicketStatusLog> statusLogs)    { this.statusLogs = statusLogs; return this; }
        public Builder editable(boolean editable)                      { this.editable = editable; return this; }

        public TicketDetailDto build() { return new TicketDetailDto(this); }
    }
}

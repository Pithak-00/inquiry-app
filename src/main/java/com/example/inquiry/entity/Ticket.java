package com.example.inquiry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<TicketComment> comments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketAttachment> attachments;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("changedAt ASC")
    private List<TicketStatusLog> statusLogs;

    public Ticket() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public enum TicketStatus {
        NEW, IN_PROGRESS, RESOLVED, CLOSED
    }

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    // Getters
    public Long getId()                          { return id; }
    public User getUser()                        { return user; }
    public String getTitle()                     { return title; }
    public String getBody()                      { return body; }
    public TicketStatus getStatus()              { return status; }
    public Priority getPriority()                { return priority; }
    public Category getCategory()                { return category; }
    public LocalDateTime getDeletedAt()          { return deletedAt; }
    public LocalDateTime getCreatedAt()          { return createdAt; }
    public LocalDateTime getUpdatedAt()          { return updatedAt; }
    public List<TicketComment> getComments()     { return comments; }
    public List<TicketAttachment> getAttachments()   { return attachments; }
    public List<TicketStatusLog> getStatusLogs() { return statusLogs; }

    // Setters
    public void setId(Long id)                             { this.id = id; }
    public void setUser(User user)                         { this.user = user; }
    public void setTitle(String title)                     { this.title = title; }
    public void setBody(String body)                       { this.body = body; }
    public void setStatus(TicketStatus status)             { this.status = status; }
    public void setPriority(Priority priority)             { this.priority = priority; }
    public void setCategory(Category category)             { this.category = category; }
    public void setDeletedAt(LocalDateTime deletedAt)      { this.deletedAt = deletedAt; }
    public void setCreatedAt(LocalDateTime createdAt)      { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt)      { this.updatedAt = updatedAt; }
    public void setComments(List<TicketComment> comments)          { this.comments = comments; }
    public void setAttachments(List<TicketAttachment> attachments) { this.attachments = attachments; }
    public void setStatusLogs(List<TicketStatusLog> statusLogs)    { this.statusLogs = statusLogs; }
}

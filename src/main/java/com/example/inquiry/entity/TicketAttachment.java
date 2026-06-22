package com.example.inquiry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_attachments")
public class TicketAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "original_name", nullable = false, length = 255)
    private String originalName;

    @Column(nullable = false, length = 500)
    private String path;

    @Column(name = "mime_type", nullable = false, length = 100)
    private String mimeType;

    @Column(nullable = false)
    private Long size;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TicketAttachment() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId()                 { return id; }
    public Ticket getTicket()           { return ticket; }
    public String getOriginalName()     { return originalName; }
    public String getPath()             { return path; }
    public String getMimeType()         { return mimeType; }
    public Long getSize()               { return size; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                         { this.id = id; }
    public void setTicket(Ticket ticket)               { this.ticket = ticket; }
    public void setOriginalName(String originalName)   { this.originalName = originalName; }
    public void setPath(String path)                   { this.path = path; }
    public void setMimeType(String mimeType)           { this.mimeType = mimeType; }
    public void setSize(Long size)                     { this.size = size; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }
}

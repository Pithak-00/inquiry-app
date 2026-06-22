package com.example.inquiry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_comments")
public class TicketComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TicketComment() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters
    public Long getId()                 { return id; }
    public Ticket getTicket()           { return ticket; }
    public User getUser()               { return user; }
    public String getBody()             { return body; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                         { this.id = id; }
    public void setTicket(Ticket ticket)               { this.ticket = ticket; }
    public void setUser(User user)                     { this.user = user; }
    public void setBody(String body)                   { this.body = body; }
    public void setCreatedAt(LocalDateTime createdAt)  { this.createdAt = createdAt; }
}

package com.example.inquiry.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_status_logs")
public class TicketStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "changed_by", nullable = false)
    private User changedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 20)
    private Ticket.TicketStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 20)
    private Ticket.TicketStatus toStatus;

    @Column(name = "changed_at", nullable = false, updatable = false)
    private LocalDateTime changedAt;

    public TicketStatusLog() {}

    @PrePersist
    protected void onCreate() {
        changedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId()                     { return id; }
    public Ticket getTicket()               { return ticket; }
    public User getChangedBy()              { return changedBy; }
    public Ticket.TicketStatus getFromStatus() { return fromStatus; }
    public Ticket.TicketStatus getToStatus()   { return toStatus; }
    public LocalDateTime getChangedAt()     { return changedAt; }

    // Setters
    public void setId(Long id)                                 { this.id = id; }
    public void setTicket(Ticket ticket)                       { this.ticket = ticket; }
    public void setChangedBy(User changedBy)                   { this.changedBy = changedBy; }
    public void setFromStatus(Ticket.TicketStatus fromStatus)  { this.fromStatus = fromStatus; }
    public void setToStatus(Ticket.TicketStatus toStatus)      { this.toStatus = toStatus; }
    public void setChangedAt(LocalDateTime changedAt)          { this.changedAt = changedAt; }
}

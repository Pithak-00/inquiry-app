package com.example.inquiry.repository;

import com.example.inquiry.entity.TicketStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketStatusLogRepository extends JpaRepository<TicketStatusLog, Long> {
    List<TicketStatusLog> findByTicketIdOrderByChangedAtAsc(Long ticketId);
}

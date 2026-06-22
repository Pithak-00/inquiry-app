package com.example.inquiry.validator;

import com.example.inquiry.entity.Ticket.TicketStatus;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class TicketStatusTransitionValidator {

    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED = Map.of(
        TicketStatus.NEW,         Set.of(TicketStatus.IN_PROGRESS),
        TicketStatus.IN_PROGRESS, Set.of(TicketStatus.RESOLVED),
        TicketStatus.RESOLVED,    Set.of(TicketStatus.IN_PROGRESS, TicketStatus.CLOSED),
        TicketStatus.CLOSED,      Set.of()
    );

    public boolean isValid(TicketStatus from, TicketStatus to) {
        Set<TicketStatus> allowed = ALLOWED.getOrDefault(from, Set.of());
        return allowed.contains(to);
    }

    public void validate(TicketStatus from, TicketStatus to) {
        if (!isValid(from, to)) {
            throw new IllegalStateException(
                String.format("ステータスを %s から %s へ変更することはできません", from, to)
            );
        }
    }
}

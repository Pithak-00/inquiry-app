package com.example.inquiry.dto;

import com.example.inquiry.entity.Ticket.TicketStatus;
import jakarta.validation.constraints.NotNull;

public class StatusChangeForm {

    @NotNull(message = "ステータスを選択してください")
    private TicketStatus toStatus;

    public StatusChangeForm() {}

    public TicketStatus getToStatus()               { return toStatus; }
    public void setToStatus(TicketStatus toStatus)  { this.toStatus = toStatus; }
}

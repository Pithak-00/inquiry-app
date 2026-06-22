package com.example.inquiry.dto;

import com.example.inquiry.entity.Ticket.Priority;
import com.example.inquiry.entity.Ticket.TicketStatus;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class TicketSearchForm {

    private String keyword;
    private TicketStatus status;
    private Priority priority;
    private Long categoryId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFrom;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTo;

    private int page = 0;

    public TicketSearchForm() {}

    // Getters
    public String getKeyword()          { return keyword; }
    public TicketStatus getStatus()     { return status; }
    public Priority getPriority()       { return priority; }
    public Long getCategoryId()         { return categoryId; }
    public LocalDate getDateFrom()      { return dateFrom; }
    public LocalDate getDateTo()        { return dateTo; }
    public int getPage()                { return page; }

    // Setters
    public void setKeyword(String keyword)              { this.keyword = keyword; }
    public void setStatus(TicketStatus status)          { this.status = status; }
    public void setPriority(Priority priority)          { this.priority = priority; }
    public void setCategoryId(Long categoryId)          { this.categoryId = categoryId; }
    public void setDateFrom(LocalDate dateFrom)         { this.dateFrom = dateFrom; }
    public void setDateTo(LocalDate dateTo)             { this.dateTo = dateTo; }
    public void setPage(int page)                       { this.page = page; }
}

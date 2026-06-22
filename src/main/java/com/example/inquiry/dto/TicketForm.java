package com.example.inquiry.dto;

import com.example.inquiry.entity.Ticket.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public class TicketForm {

    @NotBlank(message = "タイトルは必須です")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    private String title;

    @NotBlank(message = "本文は必須です")
    private String body;

    @NotNull(message = "優先度は必須です")
    private Priority priority;

    private Long categoryId;

    private List<MultipartFile> attachments;

    public TicketForm() {}

    // Getters
    public String getTitle()                    { return title; }
    public String getBody()                     { return body; }
    public Priority getPriority()               { return priority; }
    public Long getCategoryId()                 { return categoryId; }
    public List<MultipartFile> getAttachments() { return attachments; }

    // Setters
    public void setTitle(String title)                         { this.title = title; }
    public void setBody(String body)                           { this.body = body; }
    public void setPriority(Priority priority)                 { this.priority = priority; }
    public void setCategoryId(Long categoryId)                 { this.categoryId = categoryId; }
    public void setAttachments(List<MultipartFile> attachments){ this.attachments = attachments; }
}

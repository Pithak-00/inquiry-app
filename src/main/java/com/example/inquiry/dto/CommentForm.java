package com.example.inquiry.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentForm {

    @NotBlank(message = "コメントを入力してください")
    private String body;

    public CommentForm() {}

    public String getBody()           { return body; }
    public void setBody(String body)  { this.body = body; }
}

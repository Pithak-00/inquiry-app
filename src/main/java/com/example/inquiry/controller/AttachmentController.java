package com.example.inquiry.controller;

import com.example.inquiry.entity.TicketAttachment;
import com.example.inquiry.security.CustomUserDetails;
import com.example.inquiry.service.AttachmentService;
import com.example.inquiry.util.FileStorageUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final FileStorageUtil fileStorageUtil;

    public AttachmentController(AttachmentService attachmentService, FileStorageUtil fileStorageUtil) {
        this.attachmentService = attachmentService;
        this.fileStorageUtil   = fileStorageUtil;
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<byte[]> getFile(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        TicketAttachment attachment = attachmentService.getFile(
            id, userDetails.getUserId(), userDetails.isAdmin()
        );

        byte[] data = fileStorageUtil.load(attachment.getPath());

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"" + attachment.getOriginalName() + "\"")
            .contentType(MediaType.parseMediaType(attachment.getMimeType()))
            .contentLength(data.length)
            .body(data);
    }
}

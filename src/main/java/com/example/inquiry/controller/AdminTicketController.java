package com.example.inquiry.controller;

import com.example.inquiry.dto.StatusChangeForm;
import com.example.inquiry.entity.User;
import com.example.inquiry.security.CustomUserDetails;
import com.example.inquiry.service.TicketService;
import com.example.inquiry.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminTicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public AdminTicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService   = userService;
    }

    // ===== ステータス変更 =====
    @PostMapping("/tickets/{id}/status")
    public String changeStatus(
        @PathVariable Long id,
        @Valid @ModelAttribute StatusChangeForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "ステータスを選択してください");
            return "redirect:/tickets/" + id;
        }

        User admin = userService.findById(userDetails.getUserId());
        try {
            ticketService.changeStatus(id, form.getToStatus(), admin);
            redirectAttributes.addFlashAttribute("successMessage",
                "ステータスを「" + form.getToStatus() + "」に変更しました");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/tickets/" + id;
    }

    // ===== 論理削除 =====
    @PostMapping("/tickets/{id}/delete")
    public String softDelete(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        RedirectAttributes redirectAttributes
    ) {
        User admin = userService.findById(userDetails.getUserId());
        ticketService.softDelete(id, admin);
        redirectAttributes.addFlashAttribute("successMessage", "チケットを削除しました");
        return "redirect:/tickets";
    }
}

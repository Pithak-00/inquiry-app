package com.example.inquiry.controller;

import com.example.inquiry.dto.*;
import com.example.inquiry.entity.Ticket;
import com.example.inquiry.entity.User;
import com.example.inquiry.security.CustomUserDetails;
import com.example.inquiry.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TicketController {

    private final TicketService ticketService;
    private final CommentService commentService;
    private final UserService userService;

    public TicketController(
        TicketService ticketService,
        CommentService commentService,
        UserService userService
    ) {
        this.ticketService  = ticketService;
        this.commentService = commentService;
        this.userService    = userService;
    }

    // ===== チケット一覧 =====
    @GetMapping("/tickets")
    public String list(
        @ModelAttribute TicketSearchForm form,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model
    ) {
        Page<Ticket> page = ticketService.search(form, userDetails.getUserId(), userDetails.isAdmin());
        model.addAttribute("page", page);
        model.addAttribute("form", form);
        model.addAttribute("categories", ticketService.findAllCategories());
        model.addAttribute("statuses", Ticket.TicketStatus.values());
        model.addAttribute("priorities", Ticket.Priority.values());
        return "tickets/list";
    }

    // ===== チケット詳細 =====
    @GetMapping("/tickets/{id}")
    public String detail(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model
    ) {
        TicketDetailDto dto = ticketService.findDetailById(id, userDetails.getUserId(), userDetails.isAdmin());
        model.addAttribute("ticket", dto);
        model.addAttribute("commentForm", new CommentForm());
        if (userDetails.isAdmin()) {
            model.addAttribute("statusChangeForm", new StatusChangeForm());
            model.addAttribute("statuses", Ticket.TicketStatus.values());
        }
        return "tickets/detail";
    }

    // ===== チケット新規作成フォーム =====
    @GetMapping("/tickets/new")
    public String newForm(Model model) {
        model.addAttribute("form", new TicketForm());
        model.addAttribute("categories", ticketService.findAllCategories());
        model.addAttribute("priorities", Ticket.Priority.values());
        return "tickets/new";
    }

    // ===== チケット登録 =====
    @PostMapping("/tickets")
    public String create(
        @Valid @ModelAttribute("form") TicketForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", ticketService.findAllCategories());
            model.addAttribute("priorities", Ticket.Priority.values());
            return "tickets/new";
        }

        User owner = userService.findById(userDetails.getUserId());
        Ticket ticket = ticketService.create(form, owner);
        redirectAttributes.addFlashAttribute("successMessage", "チケットを登録しました");
        return "redirect:/tickets/" + ticket.getId();
    }

    // ===== チケット編集フォーム =====
    @GetMapping("/tickets/{id}/edit")
    public String editForm(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model
    ) {
        TicketDetailDto dto = ticketService.findDetailById(id, userDetails.getUserId(), userDetails.isAdmin());

        if (!dto.isEditable()) {
            return "redirect:/tickets/" + id;
        }

        TicketForm form = new TicketForm();
        form.setTitle(dto.getTitle());
        form.setBody(dto.getBody());
        form.setPriority(dto.getPriority());

        model.addAttribute("ticketId", id);
        model.addAttribute("form", form);
        model.addAttribute("ticket", dto);
        model.addAttribute("categories", ticketService.findAllCategories());
        model.addAttribute("priorities", Ticket.Priority.values());
        return "tickets/edit";
    }

    // ===== チケット更新 =====
    @PostMapping("/tickets/{id}")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute("form") TicketForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            TicketDetailDto dto = ticketService.findDetailById(id, userDetails.getUserId(), userDetails.isAdmin());
            model.addAttribute("ticketId", id);
            model.addAttribute("ticket", dto);
            model.addAttribute("categories", ticketService.findAllCategories());
            model.addAttribute("priorities", Ticket.Priority.values());
            return "tickets/edit";
        }

        ticketService.update(id, form, userDetails.getUserId());
        redirectAttributes.addFlashAttribute("successMessage", "チケットを更新しました");
        return "redirect:/tickets/" + id;
    }

    // ===== コメント投稿 =====
    @PostMapping("/tickets/{id}/comments")
    public String addComment(
        @PathVariable Long id,
        @Valid @ModelAttribute("commentForm") CommentForm form,
        BindingResult bindingResult,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("commentError", "コメントを入力してください");
            return "redirect:/tickets/" + id;
        }

        Ticket ticket = ticketService.getTicketOrThrow(id);
        User user = userService.findById(userDetails.getUserId());
        commentService.addComment(ticket, user, form, userDetails.isAdmin());

        redirectAttributes.addFlashAttribute("successMessage", "コメントを投稿しました");
        return "redirect:/tickets/" + id;
    }
}

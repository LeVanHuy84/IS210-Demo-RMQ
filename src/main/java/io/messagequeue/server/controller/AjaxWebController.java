package io.messagequeue.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import io.messagequeue.server.dto.auth.RegisterRequest;
import io.messagequeue.server.service.auth.AuthService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AjaxWebController {
    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage() {
        return "/auth/login"; // trả về file login.html trong templates
    }

    @GetMapping("web/index")
    public String showIndexPage() {
        return "index"; // maps to templates/index.html
    }

    @GetMapping("/web/carts")
    public String showCartsAjaxPage() {
        return "carts"; // maps to templates/orders_ajax.html
    }

    @GetMapping("web/orders")
    public String showOrdersAjaxPage() {
        return "orders";
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "/auth/register"; // tên file Thymeleaf: register.html
    }

    // Xử lý submit form đăng ký
    @PostMapping("/register")
    public String processRegister(@ModelAttribute RegisterRequest request, Model model) {
        try {
            authService.register(request);
            return "redirect:/login?registered"; // redirect về login sau khi đăng ký thành công
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register"; // hiển thị lại form với lỗi
        }
    }

}

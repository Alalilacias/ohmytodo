package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller()
@RequestMapping("/auth")
public class AuthWebController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, HttpServletRequest servletRequest){
        boolean isLogged = authService.login(loginRequest, servletRequest);

        if (isLogged) {
            return "index";
        } else {
            model.addAttribute("loginError", true);
            model.addAttribute("loginMessage", "Invalid credentials");
            return "login";
        }
    }
}

package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.enums.TempModalType;
import com.ohmy.todo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthWebController {

    private final AuthService authService;
    private final MessageSource messageSource;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest("", ""));
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, Model model, RedirectAttributes redirectAttributes, HttpServletRequest servletRequest){
        boolean isLogged = authService.login(loginRequest, servletRequest);

        if (isLogged) {
            String successMessage = messageSource.getMessage("auth.login.success", null, LocaleContextHolder.getLocale());

            redirectAttributes.addFlashAttribute("tempModalType", TempModalType.SUCCESS.name().toLowerCase());
            redirectAttributes.addFlashAttribute("tempModalMessage", successMessage);
            return "redirect:/index";
        } else {
            model.addAttribute("loginError", true);
            return "login";
        }
    }
}
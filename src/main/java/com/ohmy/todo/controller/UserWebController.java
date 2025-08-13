package com.ohmy.todo.controller;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.exception.UserAlreadyExistsException;
import com.ohmy.todo.model.Address;
import com.ohmy.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/users")
public class UserWebController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("registrationRequest", new UserRegistrationRequest("", "", "", new Address()));
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserRegistrationRequest userRegistrationRequest, Model model, RedirectAttributes redirectAttributes){
        try {
            userService.add(userRegistrationRequest);
        } catch (UserAlreadyExistsException alreadyExistsException) {
            model.addAttribute("registerError", true);
            model.addAttribute("registerErrorMessage", alreadyExistsException.getMessage());
            model.addAttribute("registrationRequest", userRegistrationRequest);
            return "register";
        }

        redirectAttributes.addFlashAttribute("isTempModal", true);
        redirectAttributes.addFlashAttribute("tempModalMessage", "Registration Successful! Feel free to log in.");

        return "redirect:/index";
    }
}

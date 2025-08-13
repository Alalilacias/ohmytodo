package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.service.TodoService;
import com.ohmy.todo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todos")
public class TodoWebController {

    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/create")
    public String add(Model model){
        model.addAttribute("users", userService.getAll());
        model.addAttribute("registrationRequest", new TodoRegistrationRequest(1L, "", false));

        return "createtodo";
    }
}

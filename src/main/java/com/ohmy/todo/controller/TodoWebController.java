package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.exception.UserNotAuthorizedException;
import com.ohmy.todo.exception.UserNotFoundException;
import com.ohmy.todo.service.TodoService;
import com.ohmy.todo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/todos")
public class TodoWebController {

    private final TodoService todoService;
    private final UserService userService;

    @GetMapping("/create")
    public String add(Model model){
        model.addAttribute("users", userService.getAll());
        model.addAttribute("todoRegistrationRequest", new TodoRegistrationRequest(null, "", false));

        return "createtodo";
    }

    @PostMapping("/create")
    public String add(@Valid @ModelAttribute TodoRegistrationRequest todoRegistrationRequest,
                      BindingResult bindingResult,
                      Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAll());
            model.addAttribute("registrationRequest", todoRegistrationRequest);

            return "createtodo";
        }

        try {
            todoService.add(todoRegistrationRequest);

            redirectAttributes.addFlashAttribute("tempModalType", "success");
            redirectAttributes.addFlashAttribute("tempModalMessage", "Todo successfully created!");

            return "redirect:/index";
        } catch (UserNotFoundException userNotFoundException){
            bindingResult.rejectValue("userId", "user.notFound", "Selected user does not exist.");
            model.addAttribute("users", userService.getAll());

            return "createtodo";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable long id, RedirectAttributes redirectAttributes){
        try {
            todoService.delete(id);
        } catch (UserNotAuthorizedException userNotAuthorizedException){
            redirectAttributes.addFlashAttribute("tempModalType", "danger");
            redirectAttributes.addFlashAttribute("tempModalMessage", "That TODO is not yours to eliminate");
        }

        return "redirect:/index";
    }
}
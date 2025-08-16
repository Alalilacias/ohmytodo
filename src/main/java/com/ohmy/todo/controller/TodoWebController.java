package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.exception.UserNotAuthorizedException;
import com.ohmy.todo.exception.UserNotFoundException;
import com.ohmy.todo.model.Todo;
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
            model.addAttribute("todoRegistrationRequest", todoRegistrationRequest);

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

    @GetMapping("update/{id}")
    public String update(@PathVariable long id, RedirectAttributes redirectAttributes, Model model){
        Todo todo = todoService.get(id);

        try {
            todoService.ensureOwnership(todo);
        } catch (UserNotAuthorizedException userNotAuthorizedException) {
            redirectAttributes.addFlashAttribute("tempModalType", "danger");
            redirectAttributes.addFlashAttribute("tempModalMessage", "That TODO is not yours to update");

            return "redirect:/index";
        }

        TodoUpdateRequest todoUpdateRequest = new TodoUpdateRequest(todo.getId(), todo.getTitle(), todo.isCompleted());
        model.addAttribute("todoUpdateRequest", todoUpdateRequest);

        return "updatetodo";
    }

    @PostMapping("update/{id}")
    public String update(@Valid @ModelAttribute TodoUpdateRequest todoUpdateRequest,
                         BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes){
        if (bindingResult.hasErrors()){
            model.addAttribute("users", userService.getAll());
            model.addAttribute("todoUpdateRequest", todoUpdateRequest);

            return "updatetodo";
        }

        /* Ownership already tested in get mapping. The structure of the service method is not changed to avoid refactor,
        but it is unlikely that the error will be thrown in this method, so the try/catch is avoided in this case.
         */
        todoService.update(todoUpdateRequest);

        return "redirect:/index";
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
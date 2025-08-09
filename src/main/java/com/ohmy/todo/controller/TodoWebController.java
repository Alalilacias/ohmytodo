package com.ohmy.todo.controller;

import com.ohmy.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller("/todos")
public class TodoWebController {

    private final TodoService todoService;

    @GetMapping
    public String getAllFiltered(){


        return "/index";
    }

}
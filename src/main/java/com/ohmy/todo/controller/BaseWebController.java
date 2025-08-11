package com.ohmy.todo.controller;

import com.ohmy.todo.dto.response.PageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BaseWebController {

    @GetMapping({"/", "/index"})
    public String getMain(Model model){
        model.addAttribute("pageContent",
                new PageResponse<>(
                        List.of(),
                        5,
                        1,
                        true,
                        false,
                        50,
                        150
                ));
        return "index";
    }
}

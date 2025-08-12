package com.ohmy.todo.controller;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.response.PageResponse;
import com.ohmy.todo.service.TodoService;
import com.ohmy.todo.utils.PageResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class BaseWebController {
    private final TodoService todoService;

    @GetMapping({"/", "/index"})
    public String getMain(@RequestParam(required = false) String title,
                          @RequestParam(required = false) String username,
                          @PageableDefault(size = 20, sort = "user.username", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model){
        String response = "index";
        Page<TodoDto> todoDtoPage = todoService.getAllFiltered(title, username, pageable);
        PageResponse<TodoDto> pageResponse = PageResponseMapper.toPageResponse(todoDtoPage);

        pageResponse.content().getFirst().userDto().country();

        model.addAttribute("pageResponse", pageResponse);

        return response;
    }
}

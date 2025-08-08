package com.ohmy.todo.controller.rest;

import com.ohmy.todo.controller.api.TodoApi;
import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.dto.response.PageResponse;
import com.ohmy.todo.exception.OhMyTodoError;
import com.ohmy.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TodoRestController implements TodoApi {

    private final TodoService todoService;

    @Override
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoRegistrationRequest request){
        return ResponseEntity.ok(todoService.add(request));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<CompleteTodoResponse> getOne(@PathVariable long id){
        return ResponseEntity.ok(todoService.getCompleteResponse(id));
    }

    @Override
    @GetMapping("/filter")
    public ResponseEntity<PageResponse<TodoDto>> getAllFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String username,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<TodoDto> page = todoService.getAllFiltered(text, username, pageable);
        return ResponseEntity.ok(new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getNumber(),
                page.isFirst(),
                page.isLast(),
                page.getSize(),
                page.getNumberOfElements()
        ));
    }

    @Override
    @PatchMapping
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoUpdateRequest request){
        return ResponseEntity.ok(todoService.update(request));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable long id){
        todoService.delete(id);
        return ResponseEntity.ok("Todo correctly deleted");
    }
}
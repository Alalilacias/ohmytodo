package com.ohmy.todo.controller;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos")
@Tag(name = "Todos", description = "Controller for the management of todo operations")
public class TodoController {

    private final TodoService todoService;

    @Operation(
            summary = "Create a new TODO",
            description = "Registers a new todo, with the provided data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
    })
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoRegistrationRequest request){
        return ResponseEntity.ok(todoService.add(request));
    }

    @GetMapping
    public ResponseEntity<List<TodoDto>> getAll(){
        return ResponseEntity.ok(todoService.getAll());
    }
}

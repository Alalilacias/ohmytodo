package com.ohmy.todo.controller;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.model.Todo;
import com.ohmy.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
            description = "Registers a new todo using the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoRegistrationRequest request){
        return ResponseEntity.ok(todoService.add(request));
    }

    @Operation(
            summary = "Get a TODO by ID",
            description = "Retrieves a specific todo using its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Todo not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getOne(@PathVariable long id){
        return ResponseEntity.ok(todoService.get(id));
    }

    @Operation(
            summary = "Get all TODOs",
            description = "Fetches a list of all todos available in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todos successfully retrieved")
            }
    )
    @GetMapping
    public ResponseEntity<List<TodoDto>> getAll(){
        return ResponseEntity.ok(todoService.getAll());
    }

    @Operation(
            summary = "Filter TODOs",
            description = "Fetches a list of todos matching the given filters: partial text match and exact username.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Filtered todos successfully retrieved")
            }
    )
    @GetMapping("/filter")
    public ResponseEntity<List<TodoDto>> getAllFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String username
    ) {
        return ResponseEntity.ok(todoService.getAllFiltered(text, username));
    }
}

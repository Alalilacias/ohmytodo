package com.ohmy.todo.controller;

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
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Todos", description = "Controller for the management of todo operations")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create a new TODO",
            description = "Registers a new todo using the provided data",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Todo information for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoRegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@RequestBody @Valid TodoRegistrationRequest request){
        return ResponseEntity.ok(todoService.add(request));
    }

    @Operation(summary = "Get a TODO by ID",
            description = "Retrieves a specific todo using its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Todo not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CompleteTodoResponse> getOne(@PathVariable long id){
        return ResponseEntity.ok(todoService.getCompleteResponse(id));
    }

    @Operation(
            summary = "Get filtered TODOs",
            description = "Retrieves a paginated list of TODOs filtered by text and/or username",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Todos successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid pagination parameters",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
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


    @Operation(summary = "Update an existing Todo.",
            description = "Compares the given TODOs information and modifies the modifiable parts." +
                    "Only allows modifying title and completed at the moment.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Correctly updated.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — authentication is required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Todo does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
    @PatchMapping
    public ResponseEntity<TodoDto> updateTodo(@RequestBody TodoUpdateRequest request){
        return ResponseEntity.ok(todoService.update(request));
    }

    @Operation(summary = "Delete an existing Todo.",
            description = "Deletes referenced ID, if the authenticated user is the owner of it.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Correctly Deleted.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TodoDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — authentication is required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Todo does not exist",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable long id){
        todoService.delete(id);
        return ResponseEntity.ok("Todo correctly deleted");
    }
}

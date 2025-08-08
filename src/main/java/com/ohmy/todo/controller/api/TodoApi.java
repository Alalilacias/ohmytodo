package com.ohmy.todo.controller.api;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.dto.response.PageResponse;
import com.ohmy.todo.exception.OhMyTodoError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Todos", description = "Controller for the management of todo operations")
public interface TodoApi {

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
    ResponseEntity<TodoDto> createTodo(TodoRegistrationRequest request);

    @Operation(summary = "Get a TODO by ID",
            description = "Retrieves a specific todo using its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Todo successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Todo not found")
            }
    )
    ResponseEntity<CompleteTodoResponse> getOne(@PathVariable long id);

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
    ResponseEntity<PageResponse<TodoDto>> getAllFiltered(String text, String username, Pageable pageable);

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
    ResponseEntity<TodoDto> updateTodo(@RequestBody TodoUpdateRequest request);

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
    ResponseEntity<String> deleteTodo(@PathVariable long id);
}
package com.ohmy.todo.controller;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.exception.OhMyTodoError;
import com.ohmy.todo.model.User;
import com.ohmy.todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Controller for the management of user operations")
public class UserController {

    final private UserService userService;

    @Operation(
            summary = "Create a new user",
            description = "Registers a new user using the provided registration details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserRegistrationRequest request) {
        UserDto createdUser = userService.add(request);

        return ResponseEntity.ok(createdUser);
    }


    @Operation(summary = "Get user by ID",
            description = "Checks if a user with the specified ID exists, returns it's entire data structure if it does",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "User successfully returned",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "User does not exist",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized — authentication is required",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                )
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@RequestBody long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Get a list of the DTOs of all users. This does not require authentication, as per the user story",
            description = "Returns a list containing the username and ID of all users."
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Delete a specific user, using their ID. This requires authentication",
            description = "Deletes the user with the given ID. Requires authentication.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Correctly returned",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
    })
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestBody long id){
        return ResponseEntity.ok(userService.delete(id));
    }
}

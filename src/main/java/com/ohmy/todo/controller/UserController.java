package com.ohmy.todo.controller;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.exception.OhMyTodoError;
import com.ohmy.todo.model.User;
import com.ohmy.todo.service.AuthService;
import com.ohmy.todo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Controller for the management of user operations")
public class UserController {

    final private UserService userService;
    final private AuthService authService;

    @Operation(
            summary = "Create a new user",
            description = "Registers a new user using the provided registration details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "User already exists")
            }
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.add(request));
    }


    @Operation(summary = "Get the currently authenticated user",
            description = "Returns the full user entity of the authenticated user using the security context.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully returned",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — authentication is required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(userService.getUserBySecurityContextHolder());
    }

    @Operation(summary = "Get a list of the DTOs of all users. This does not require authentication, as per the user story",
            description = "Returns a list containing the username and ID of all users.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "correctly returned",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = UserDto.class))
                        )
                )
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @Operation(summary = "Delete the currently authenticated user",
            description = "Deletes the authenticated user based on the security context, then logs them out.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User deleted and logged out successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class))
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
            }
    )
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(HttpServletRequest request, HttpServletResponse response){
        boolean isDeleted = userService.deleteBySecurityContextHolder();
        boolean isLoggedOut = authService.logout(request, response);

        return ResponseEntity.ok(isDeleted && isLoggedOut);
    }
}

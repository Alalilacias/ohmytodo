package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.exception.OhMyTodoError;
import com.ohmy.todo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Controller for the management of authentication operations")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authenticate a user",
            description = "Authenticates the user with the provided login credentials and stores the session using cookies.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        boolean success = authService.login(request, httpRequest);
        if (success) return ResponseEntity.ok("Login successful");
        return ResponseEntity.status(401).body("Login failed");
    }

    @Operation(
            summary = "Log out the currently authenticated user",
            description = "Logs the user out by invalidating the session and clearing authentication-related data.",
            security = @SecurityRequirement(name = "cookieAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Logout successful",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — authentication is required",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected server error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OhMyTodoError.class))
                    )
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean success = authService.logout(request, response);
        return ResponseEntity.ok("Logout successful: " + success);
    }
}

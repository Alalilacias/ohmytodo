package com.ohmy.todo.controller;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.service.AuthService;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Schema(name = "Authentication", description = "Controller for the management of authentication operations")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        boolean success = authService.login(request, httpRequest);
        if (success) return ResponseEntity.ok("Login successful");
        return ResponseEntity.status(401).body("Login failed");
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean success = authService.logout(request, response);
        return ResponseEntity.ok("Logout successful: " + success);
    }
}
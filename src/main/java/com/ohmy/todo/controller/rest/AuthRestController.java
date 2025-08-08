package com.ohmy.todo.controller.rest;

import com.ohmy.todo.controller.api.AuthApi;
import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthRestController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        boolean success = authService.login(request, httpRequest);
        if (success) return ResponseEntity.ok("Login successful");
        return ResponseEntity.status(401).body("Login failed");
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        boolean success = authService.logout(request, response);
        return ResponseEntity.ok("Logout successful: " + success);
    }
}
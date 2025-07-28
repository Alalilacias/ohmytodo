package com.ohmy.todo.service;

import com.ohmy.todo.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean login(LoginRequest loginRequest, HttpServletRequest request);
    boolean logout(HttpServletRequest request, HttpServletResponse response);
}

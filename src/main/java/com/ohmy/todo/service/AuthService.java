package com.ohmy.todo.service;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean login(LoginRequest loginRequest, HttpServletRequest request);
    boolean logout(HttpServletRequest request, HttpServletResponse response);
    User getUserBySecurityContextHolder();
    long getUserIdBySecurityContextHolder();
}

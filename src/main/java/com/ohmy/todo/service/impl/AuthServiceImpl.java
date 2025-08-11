package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.request.LoginRequest;
import com.ohmy.todo.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean login(LoginRequest loginRequest, HttpServletRequest request) {
        log.debug("Attempting authentication for user '{}'", loginRequest.username());

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        try {
            Authentication auth = authenticationManager.authenticate(token);

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            HttpSession session = request.getSession(true); // creates session if it doesn't exist
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            log.debug("User '{}' authenticated successfully. Session created.", loginRequest.username());
            return true;
        } catch (AuthenticationException e) {
            log.warn("Authentication failed for user '{}': {}", loginRequest.username(), e.getMessage());
            return false;
        }
    }

    @Override
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            log.debug("No authenticated user found. Skipping logout.");
            return false;
        }

        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            log.debug("Session invalidated");
        } else {
            log.debug("No session to invalidate");
        }

        SecurityContextHolder.clearContext();
        log.debug("Security context cleared");

        Cookie cookie = new Cookie("SESSION", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        log.debug("SESSION cookie deleted");

        return true;
    }


}
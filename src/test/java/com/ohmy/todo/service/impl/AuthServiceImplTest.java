package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.request.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthServiceImpl authService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("user", "pass");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(request.getSession(true)).thenReturn(session);

        boolean result = authService.login(loginRequest, request);

        assertTrue(result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(request).getSession(true);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLoginFailure() {
        LoginRequest loginRequest = new LoginRequest("user", "wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        boolean result = authService.login(loginRequest, request);

        assertFalse(result);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogoutWithSession() {
        when(request.getSession(false)).thenReturn(session);

        boolean result = authService.logout(request, response);

        assertTrue(result);
        verify(session).invalidate();
        verify(response).addCookie(argThat(cookie ->
                cookie.getName().equals("JSESSIONID") && cookie.getMaxAge() == 0));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testLogoutWithoutSession() {
        when(request.getSession(false)).thenReturn(null);

        boolean result = authService.logout(request, response);

        assertTrue(result);
        verify(response).addCookie(argThat(cookie ->
                cookie.getName().equals("JSESSIONID") && cookie.getMaxAge() == 0));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
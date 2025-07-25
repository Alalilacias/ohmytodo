package com.ohmy.todo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleOhMyTodoException() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getRequestURI()).thenReturn("/users");

        OhMyTodoException ex = new UserAlreadyExistsException("Miranda");

        ResponseEntity<OhMyTodoError> response = handler.handleOhMyTodoException(ex, mockRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User Miranda already exists.", response.getBody().getMessage());
        assertEquals("/users", response.getBody().getPath());
    }
}
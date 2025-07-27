package com.ohmy.todo.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OhMyTodoException.class)
    public ResponseEntity<OhMyTodoError> handleOhMyTodoException(OhMyTodoException ex, HttpServletRequest request) {
        log.warn("Handled OhMyTodoException at {}: {}", request.getRequestURI(), ex.getMessage());
        OhMyTodoError error = new OhMyTodoError(ex.getStatus(), ex.getMessage(), request.getRequestURI());
        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(HttpServletRequest request, AccessDeniedException ex) {
        log.warn("Access denied at {} | Auth: {}", request.getRequestURI(),
                SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<OhMyTodoError> handleConstraintViolations(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        OhMyTodoError error = new OhMyTodoError(
                HttpStatus.BAD_REQUEST,
                "Invalid input. Please check your request and try again.",
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<OhMyTodoError> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Duplicate or invalid data.";
        if (ex.getMostSpecificCause().getMessage().contains("todos_pkey")) {
            message = "A todo with this ID already exists.";
        }

        log.warn("Data integrity error at {}: {}", request.getRequestURI(), ex.getMessage());
        OhMyTodoError error = new OhMyTodoError(HttpStatus.CONFLICT, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<OhMyTodoError> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        OhMyTodoError error = new OhMyTodoError(
                HttpStatus.BAD_REQUEST,
                "Invalid input. Please check your request and try again.",
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<OhMyTodoError> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        String message = "Missing parameter: " + ex.getParameterName();
        log.warn("Missing parameter at {}: {}", request.getRequestURI(), message);
        OhMyTodoError error = new OhMyTodoError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<OhMyTodoError> handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request){
        String message = String.format("No resource found at endpoint: /%s. ", ex.getResourcePath());
        log.warn(message);
        OhMyTodoError error = new OhMyTodoError(HttpStatus.NOT_FOUND, message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<OhMyTodoError> handleUnhandledException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception at {}: {}", request.getRequestURI(), ex.toString(), ex);
        OhMyTodoError error = new OhMyTodoError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
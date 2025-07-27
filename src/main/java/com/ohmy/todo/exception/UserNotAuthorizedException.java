package com.ohmy.todo.exception;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends OhMyTodoException {

    public UserNotAuthorizedException() {
        super("User is not authenticated or authorized to perform this action", HttpStatus.UNAUTHORIZED);
    }

    public UserNotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
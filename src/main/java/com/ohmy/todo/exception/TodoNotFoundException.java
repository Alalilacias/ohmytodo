package com.ohmy.todo.exception;

import org.springframework.http.HttpStatus;

public class TodoNotFoundException extends OhMyTodoException {
    public TodoNotFoundException(long id){
        super("Todo not found with ID: " + id, HttpStatus.NOT_FOUND);
    }
}

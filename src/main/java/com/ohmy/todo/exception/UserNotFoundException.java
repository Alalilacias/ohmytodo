package com.ohmy.todo.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends OhMyTodoException {
  public UserNotFoundException(long id) {
    super("User with ID: " + id + " does not exist.", HttpStatus.NOT_FOUND);
  }

  public UserNotFoundException(String username) {
    super("User with username: " + username + " does not exist.", HttpStatus.NOT_FOUND);
  }
}

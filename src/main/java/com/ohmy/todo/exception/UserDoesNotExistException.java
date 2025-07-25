package com.ohmy.todo.exception;

import org.springframework.http.HttpStatus;

public class UserDoesNotExistException extends OhMyTodoException {
  public UserDoesNotExistException(long id) {
    super("User with ID: " + id + " does not exist.", HttpStatus.NOT_FOUND);
  }

  public UserDoesNotExistException(String username) {
    super("User with username: " + username + " does not exist.", HttpStatus.NOT_FOUND);
  }
}

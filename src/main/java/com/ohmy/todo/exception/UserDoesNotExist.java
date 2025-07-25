package com.ohmy.todo.exception;

import org.springframework.http.HttpStatus;

public class UserDoesNotExist extends OhMyTodoException {
  public UserDoesNotExist(long id) {
    super("User with ID: " + id + " does not exist.", HttpStatus.NOT_FOUND); // 404
  }

  public UserDoesNotExist(String username) {
    super("User with username: " + username + " does not exist.", HttpStatus.NOT_FOUND); // 404
  }
}

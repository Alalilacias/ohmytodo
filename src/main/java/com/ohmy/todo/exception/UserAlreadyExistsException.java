package com.ohmy.todo.exception;

import com.ohmy.todo.model.User;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends OhMyTodoException {
  public UserAlreadyExistsException(String username) {
    super("User " + username + " already exists.", HttpStatus.CONFLICT);
  }
  public UserAlreadyExistsException(User user) {
    this(user.getUsername());
  }
}
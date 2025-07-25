package com.ohmy.todo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class OhMyTodoException extends RuntimeException {
  private final HttpStatus status;

  public OhMyTodoException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
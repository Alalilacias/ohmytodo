package com.ohmy.todo.service;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;

import java.util.List;

public interface TodoService {
    public TodoDto add(TodoRegistrationRequest request);
    public TodoDto get(long id);
    public List<TodoDto> getAll();
    public boolean update();
    public boolean delete();
}
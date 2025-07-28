package com.ohmy.todo.service;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.model.Todo;

import java.util.List;

public interface TodoService {
    public TodoDto add(TodoRegistrationRequest request);
    public Todo get(long id);
    public List<TodoDto> getAll();

    List<TodoDto> getAllFiltered(String username, String text);

    public boolean update();
    public boolean delete();
}
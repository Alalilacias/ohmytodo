package com.ohmy.todo.service;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.model.Todo;

import java.util.List;

public interface TodoService {
    TodoDto add(TodoRegistrationRequest request);
    CompleteTodoResponse getCompleteResponse(long id);
    List<TodoDto> getAll();
    List<TodoDto> getAllFiltered(String username, String text);
    TodoDto update(TodoUpdateRequest request);
    public boolean delete();
}
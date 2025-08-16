package com.ohmy.todo.service;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {
    TodoDto add(TodoRegistrationRequest request);
    CompleteTodoResponse getCompleteResponse(long id);

    Todo get(long id);

    Page<TodoDto> getAllFiltered(String text, String username, Pageable pageable);
    TodoDto update(TodoUpdateRequest request);
    void delete(long id);

    void ensureOwnership(Todo todo);
}
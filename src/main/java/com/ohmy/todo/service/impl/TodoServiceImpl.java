package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.exception.UserDoesNotExistException;
import com.ohmy.todo.model.Todo;
import com.ohmy.todo.model.User;
import com.ohmy.todo.repository.TodoRepository;
import com.ohmy.todo.repository.UserRepository;
import com.ohmy.todo.service.TodoService;
import com.ohmy.todo.utils.TodoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Override
    public TodoDto add(TodoRegistrationRequest request) {
        log.info("Creating TODO for user ID {} with title '{}'", request.userId(), request.title());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", request.userId());
                    return new UserDoesNotExistException(request.userId());
                });

        Todo todo = Todo.builder()
                .title(request.title())
                .completed(request.completed())
                .user(user)
                .build();

        TodoDto todoDto = TodoMapper.toDto(todoRepository.save(todo));

        log.info("Successfully created TODO with ID {} for user '{}'", todo.getId(), todo.getUser().getUsername());

        return todoDto;
    }

    @Override
    public TodoDto get(long id) {
        return null;
    }

    @Override
    public List<TodoDto> getAll() {
        log.info("Fetching all todos");
        return todoRepository.customFindAll()
                .stream()
                .map(TodoMapper::toDto)
                .toList();
    }

    @Override
    public boolean update() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }
}

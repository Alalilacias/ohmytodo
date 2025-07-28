package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.exception.TodoNotFoundException;
import com.ohmy.todo.exception.UserNotAuthorizedException;
import com.ohmy.todo.exception.UserNotFoundException;
import com.ohmy.todo.model.Todo;
import com.ohmy.todo.model.User;
import com.ohmy.todo.repository.TodoRepository;
import com.ohmy.todo.repository.UserRepository;
import com.ohmy.todo.service.AuthService;
import com.ohmy.todo.service.TodoService;
import com.ohmy.todo.utils.TodoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public TodoDto add(TodoRegistrationRequest request) {
        log.info("Creating TODO for user ID {} with title '{}'", request.userId(), request.title());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", request.userId());
                    return new UserNotFoundException(request.userId());
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

    // En este caso devolvemos la entidad entera para que pueda ser accedida y revisada desde Postman por quien revise
    // este repositorio. En un entorno de producción, se devolvería un DTO.
    @Transactional
    @Override
    public CompleteTodoResponse getCompleteResponse(long id){
        return TodoMapper.toCompleteResponse(get(id));
    }

    private Todo get(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @Transactional
    @Override
    public List<TodoDto> getAll() {
        log.info("Fetching all todos");
        return todoRepository.findAll()
                .stream()
                .map(TodoMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public List<TodoDto> getAllFiltered(String text, String username) {
        log.info("Fetching all todos where text: {}, and username: {}", text, username);
        if (text == null && username == null) {return getAll();}

        List<Todo> todos = todoRepository.findAllFiltered(text, username);

        return todos.stream().map(TodoMapper::toDto).toList();
    }

    @Transactional
    @Override
    public TodoDto update(TodoUpdateRequest request) {
        Todo todo = get(request.todoId());

        ensureOwnership(todo);

        todo.setTitle(request.title());
        todo.setCompleted(request.completed());

        return TodoMapper.toDto(todoRepository.save(todo));
    }

    @Override
    public boolean delete() {
        return false;
    }

    private void ensureOwnership(Todo todo) {
        User user = authService.getUserBySecurityContextHolder();
        if (!todo.getUser().getId().equals(user.getId())) {
            log.warn("User ID {} is not authorized to access Todo ID {}", user.getId(), todo.getId());
            throw new UserNotAuthorizedException();
        }
    }
}
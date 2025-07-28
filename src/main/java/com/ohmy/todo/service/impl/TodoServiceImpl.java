package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.exception.TodoNotFoundException;
import com.ohmy.todo.exception.UserNotFoundException;
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
    @Override
    public Todo get(long id) {
        return todoRepository.customFindById(id)
                .orElseThrow(() -> {
                        log.debug("Todo with ID {} not found.", id);
                        return new TodoNotFoundException(id);
                });
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
    public List<TodoDto> getAllFiltered(String text, String username) {
        log.info("Fetching all todos where text: {}, and username: {}", text, username);
        if (text == null && username == null) {return getAll();}
//        if (username == null) { username = "";}
//        if (text == null) { text = "";}

        List<Todo> todos = todoRepository.findAllFiltered(text, username);

        return todos.stream().map(TodoMapper::toDto).toList();
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

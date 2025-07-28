package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.request.TodoRegistrationRequest;
import com.ohmy.todo.dto.request.TodoUpdateRequest;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
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
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    @Override
    public CompleteTodoResponse getCompleteResponse(long id){
        return TodoMapper.toCompleteResponse(get(id));
    }

    private Todo get(long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        // Soy consciente de que el ejercicio pide priorizar evitar anotaciones de Hibernate, pero para mantener la
        // anotación de ManyToOne y su uso más eficiente para el programa y permitir que se devuelva el usuario completo
        // esta es la mejor opción.
//        Hibernate.initialize(todo.getUser());

        return todo;
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
        userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("User with ID: {} does not exist.", request.userId());
                    return new UserNotFoundException(request.userId());
                });

        todo.setTitle(request.title());
        todo.setCompleted(request.completed());

        return TodoMapper.toDto(todoRepository.save(todo));
    }

    @Override
    public boolean delete() {
        return false;
    }
}
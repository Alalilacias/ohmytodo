package com.ohmy.todo.utils;

import com.ohmy.todo.dto.TodoDto;
import com.ohmy.todo.dto.response.CompleteTodoResponse;
import com.ohmy.todo.model.Todo;

public final class TodoMapper {
    private TodoMapper(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static TodoDto toDto(Todo todo){
        return new TodoDto(todo.getId(), todo.getTitle(), todo.isCompleted(), UserMapper.toDto(todo.getUser()));
    }
    public static CompleteTodoResponse toCompleteResponse(Todo todo) {
        return new CompleteTodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.isCompleted(),
                todo.getCreatedAt(),
                todo.getCompletedAt(),
                todo.getTimeOpen(),
                UserMapper.toCompleteUserDto(todo.getUser())
        );
    }
}

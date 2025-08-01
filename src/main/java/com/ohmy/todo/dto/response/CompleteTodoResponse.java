package com.ohmy.todo.dto.response;

import com.ohmy.todo.model.User;

import java.time.LocalDateTime;

public record CompleteTodoResponse(
        Long id,
        String title,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        Long timeOpen,
        CompleteUserDto user
) {}
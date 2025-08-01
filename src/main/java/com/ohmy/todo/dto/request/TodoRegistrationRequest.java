package com.ohmy.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TodoRegistrationRequest(
        @Positive(message = "UserID must be positive")
        long userId,
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not be longer than 200 characters")
        String title,
        boolean completed
) {}
package com.ohmy.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TodoRegistrationRequest(
        @Positive(message = "UserID must be positive")
        long userId,
        @Size(max = 200, message = "{todo.title.size}")
        @NotBlank(message = "{todo.title.required}")
        String title,
        boolean completed
) {}
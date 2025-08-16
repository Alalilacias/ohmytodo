package com.ohmy.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TodoRegistrationRequest(
        @NotNull(message="{todo.userId.required}")
        @Positive(message="{todo.userId.positive}")
        Long userId,
        @NotBlank(message="{todo.title.required}")
        @Size(max=200, message="{todo.title.size}")
        String title,
        boolean completed
) {}

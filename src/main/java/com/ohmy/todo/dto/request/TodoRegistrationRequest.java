package com.ohmy.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TodoRegistrationRequest(
        @Positive long userId,
        @NotBlank @Size(max = 200) String title,
        boolean completed
) {}
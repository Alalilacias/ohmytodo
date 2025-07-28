package com.ohmy.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// Si bien idéntica a la petición de registro, para evitar problemas si en un futuro se han de modificar o diferenciar
// los procesos de creación o actualización de todos, se mantienen por separado estas peticiones.
public record TodoUpdateRequest(
        @Positive(message = "UserID must be positive")
        long userId,
        @Positive(message = "TodoID must be positive")
        long todoId,
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not be longer than 200 characters")
        String title,
        boolean completed
) {}

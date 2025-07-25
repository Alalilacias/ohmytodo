package com.ohmy.todo.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Schema(description = "Structure for API error responses")
public class OhMyTodoError {

    @Schema(description = "Timestamp of the error", example = "2025-07-20T17:22:13.519")
    private final LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "HTTP status code", example = "404")
    private final int status;

    @Schema(description = "HTTP reason phrase", example = "Not Found")
    private final String error;

    @Schema(description = "Error message", example = "Todo not found with id: 7")
    private final String message;

    @Schema(description = "Request path where the error occurred", example = "/api/todos/7")
    private final String path;

    public OhMyTodoError(HttpStatus status, String message, String path) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
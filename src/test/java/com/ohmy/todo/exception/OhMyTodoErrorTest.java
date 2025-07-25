package com.ohmy.todo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class OhMyTodoErrorTest {

    @Test
    void testConstructorSetsFieldsCorrectly() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Todo not found with id: 7";
        String path = "/api/todos/7";

        OhMyTodoError error = new OhMyTodoError(status, message, path);

        assertEquals(status.value(), error.getStatus());
        assertEquals(status.getReasonPhrase(), error.getError());
        assertEquals(message, error.getMessage());
        assertEquals(path, error.getPath());
        assertNotNull(error.getTimestamp());
    }
}
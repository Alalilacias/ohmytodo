package com.ohmy.todo.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime done = now.plusHours(1);

        Todo todo = Todo.builder()
                .id(42L)
                .title("Finish report")
                .completed(true)
                .createdAt(now)
                .completedAt(done)
                .timeOpen(Duration.between(now, done).getSeconds())
                .build();

        assertEquals(42L, todo.getId());
        assertEquals("Finish report", todo.getTitle());
        assertTrue(todo.isCompleted());
        assertEquals(now, todo.getCreatedAt());
        assertEquals(done, todo.getCompletedAt());
        assertEquals(Duration.between(now, done).getSeconds(), todo.getTimeOpen());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Write tests");
        todo.setCompleted(false);

        assertEquals(1L, todo.getId());
        assertEquals("Write tests", todo.getTitle());
        assertFalse(todo.isCompleted());
    }

    @Test
    void testOnCreate() {
        Todo todo = new Todo();
        todo.onCreate();

        assertNotNull(todo.getCreatedAt());
        assertFalse(todo.isCompleted());
    }

    @Test
    void testOnUpdate_CompletesTaskAndCalculatesTimeOpen() {
        Todo todo = new Todo();
        todo.onCreate();

        todo.setCompleted(true);

        todo.onUpdate();

        assertNotNull(todo.getCompletedAt());
        assertNotNull(todo.getTimeOpen());
        assertTrue(todo.getTimeOpen() >= 0);
    }

    @Test
    void testOnUpdate_AlreadyCompletedDoesNotOverwrite() {
        Todo todo = new Todo();
        todo.onCreate();

        LocalDateTime manualCompletedAt = LocalDateTime.now().plusMinutes(5);
        todo.setCompleted(true);
        todo.setCompletedAt(manualCompletedAt);

        todo.onUpdate(); // Should NOT overwrite completedAt

        assertEquals(manualCompletedAt, todo.getCompletedAt());
    }
}
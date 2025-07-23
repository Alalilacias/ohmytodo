package com.ohmy.todo.repository;

import com.ohmy.todo.model.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void testSaveAndFindById() {
        Todo todo = new Todo();
        todo.setTitle("Test TODO");
        todo.setCompleted(false);

        Todo saved = todoRepository.save(todo);

        Optional<Todo> retrieved = todoRepository.findById(saved.getId());

        assertThat(retrieved).isPresent();
        assertThat(retrieved.get().getTitle()).isEqualTo("Test TODO");
    }
}
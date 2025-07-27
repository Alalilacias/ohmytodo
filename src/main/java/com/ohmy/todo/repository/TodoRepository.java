package com.ohmy.todo.repository;

import com.ohmy.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("SELECT t FROM Todo t JOIN FETCH t.user")
    List<Todo> customFindAll();
}

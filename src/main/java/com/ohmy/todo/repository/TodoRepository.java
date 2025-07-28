package com.ohmy.todo.repository;

import com.ohmy.todo.model.Todo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("""
    SELECT t FROM Todo t
    WHERE (:text IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :text, '%')))
    AND (:username IS NULL OR t.user.username = :username)
""")
    List<Todo> findAllFiltered(@Param("text") String text, @Param("username") String username);
}
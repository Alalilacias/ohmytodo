package com.ohmy.todo.repository;

import com.ohmy.todo.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("""
    SELECT t FROM Todo t
    WHERE (:text IS NULL OR :text = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :text, '%')))
      AND (:username IS NULL OR :username = '' OR LOWER(t.user.username) = LOWER(:username))
    """)
    Page<Todo> findAllFiltered(
            @Param("text") String text,
            @Param("username") String username,
            Pageable pageable
    );
}
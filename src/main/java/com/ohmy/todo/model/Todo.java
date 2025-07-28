package com.ohmy.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table(name = "todos")
@Schema(name = "Todo", description = "Task to be done, tracked over time")
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the todo", example = "42")
    private Long id;

    @Schema(description = "Title of the task", example = "Finish report")
    @Column(length = 200)
    private String title;

    @Schema(description = "Whether the task is completed", example = "false")
    private boolean completed;

    // Podría ser necesario de cara a analíticas o ampliaciones de funcionalidad tener esta información
    @Schema(description = "Creation timestamp", example = "2025-07-20T12:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Completion timestamp", example = "2025-07-21T16:00:00")
    private LocalDateTime completedAt;

    @Schema(description = "Time open in seconds", example = "86400")
    private Long timeOpen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Schema(description = "Owner of the task", implementation = User.class)
    private User user;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        if (this.completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
            this.timeOpen = Duration.between(this.createdAt, this.completedAt).getSeconds();
        }
    }
}
package com.ohmy.todo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Table(name = "users")
@Schema(name = "User", description = "User entity with embedded address information")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID (auto-generated)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Full name of the user", example = "Yolanda Winston")
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username used for login", example = "Captain Commodore Steele")
    private String username;

    @Column(nullable = false)
    @Schema(description = "User password (hashed)", example = "WinstonAndFergusonAreLovers")
    private String password;

    @Embedded
    @Schema(description = "User's address information", implementation = Address.class)
    private Address address;
}

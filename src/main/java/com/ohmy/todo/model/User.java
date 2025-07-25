package com.ohmy.todo.model;

import com.ohmy.todo.enums.Role;
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

    @Schema(description = "Full username of the user", example = "Yolanda Winston")
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username used for login", example = "Captain Commodore Steele")
    private String username;

    @Column(nullable = false)
    @Schema(description = "User password (hashed)")
    private String password;

    @Embedded
    @Schema(description = "User's address information", implementation = Address.class)
    private Address address;

    // En este proyecto, solo se pueden crear usuarios con rol de "USER", ya sea por endpoint o servicio.
    // Otros roles se podrían crear mediante docker-compose o directamente en la base de datos, para testing o producción.
    // Me pareció necesario incluir la variable tanto para Redis como para posibles actualizaciones en un futuro y no me costó nada.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "User role", example = "USER")
    private Role role;
}

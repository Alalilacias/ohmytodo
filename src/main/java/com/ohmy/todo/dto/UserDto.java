package com.ohmy.todo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserDto", description = "Data Transfer Object for the User entity. Contains minimal information.")
public record UserDto(long id, String username, String country) {}

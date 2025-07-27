package com.ohmy.todo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginRequest", description = "Data transfer object, for login information.")
public record LoginRequest(String username, String password) {}

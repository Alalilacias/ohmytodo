package com.ohmy.todo.dto.request;

import com.ohmy.todo.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserRegistrationRequest", description = "Data transfer object, for user registration.")
public record UserRegistrationRequest(
        String name,
        String username,
        String password,
        Address address
) {}

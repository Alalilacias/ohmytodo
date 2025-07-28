package com.ohmy.todo.dto.response;

import com.ohmy.todo.enums.Role;
import com.ohmy.todo.model.Address;

public record CompleteUserDto(
        Long id,
        String name,
        String username,
        String password,
        Address address,
        Role role
) {}
package com.ohmy.todo.dto.request;

import com.ohmy.todo.model.Address;

public record UserRegistrationRequest(String name,String username,String password,Address address) {}

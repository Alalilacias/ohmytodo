package com.ohmy.todo.service;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.model.User;

import java.util.List;

public interface UserService {
    public UserDto add(UserRegistrationRequest request);
    public List<UserDto> getAll();
    boolean deleteBySecurityContextHolder();
}

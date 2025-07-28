package com.ohmy.todo.dto;

public record TodoDto(long id, String title, boolean completed, UserDto userDto) {}

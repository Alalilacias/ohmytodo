package com.ohmy.todo.utils;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.response.CompleteUserDto;
import com.ohmy.todo.model.User;

// La estructuración de esta clase se debe a este artículo: https://stackoverflow.com/a/31410051
public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getAddress().getCountry());
    }
    public static CompleteUserDto toCompleteUserDto(User user){
        return new CompleteUserDto(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getAddress(),
                user.getRole()
        );
    }
}
package com.ohmy.todo.controller.rest;

import com.ohmy.todo.controller.api.UserApi;
import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.model.User;
import com.ohmy.todo.service.AuthService;
import com.ohmy.todo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserRestController implements UserApi {

    final private UserService userService;
    final private AuthService authService;

    @Override
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserRegistrationRequest request) {
        return ResponseEntity.ok(userService.add(request));
    }

    @Override
    @GetMapping
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(userService.getUserBySecurityContextHolder());
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(HttpServletRequest request, HttpServletResponse response){
        boolean isDeleted = userService.deleteBySecurityContextHolder();
        boolean isLoggedOut = authService.logout(request, response);

        return ResponseEntity.ok(isDeleted && isLoggedOut);
    }
}

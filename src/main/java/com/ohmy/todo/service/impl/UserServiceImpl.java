package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.enums.Role;
import com.ohmy.todo.exception.UserAlreadyExistsException;
import com.ohmy.todo.exception.UserDoesNotExist;
import com.ohmy.todo.model.User;
import com.ohmy.todo.repository.UserRepository;
import com.ohmy.todo.service.UserService;
import com.ohmy.todo.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto add(UserRegistrationRequest request) {
        log.debug("Attempting to register user with username: {}", request.username());

        if (doesUserExist(request.username())) {
            log.debug("User with username '{}' already exists", request.username());
            throw new UserAlreadyExistsException(request.username());
        }

        User user = User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .address(request.address())
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        log.debug("User '{}' registered successfully with ID: {}", savedUser.getUsername(), savedUser.getId());

        return UserMapper.toDto(savedUser);
    }

    @Override
    public User getById(long id) {
        log.debug("Fetching user by ID: {}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("User with ID {} not found", id);
                    return new UserDoesNotExist(id);
                });
    }

    @Override
    public List<User> getAll() {
        log.debug("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public boolean delete(long id) {
        log.debug("Attempting to delete user with ID: {}", id);

        if (!doesUserExist(id)) {
            log.debug("User with ID {} does not exist", id);
            throw new UserDoesNotExist(id);
        }

        userRepository.deleteById(id);
        log.debug("User with ID {} deleted successfully", id);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.debug("User with username '{}' not found", username);
                    return new UserDoesNotExist(username);
                });

        log.debug("User '{}' found. Returning UserDetails", username);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    private boolean doesUserExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }
    private boolean doesUserExist(long id){
        return userRepository.findById(id).isPresent();
    }
}
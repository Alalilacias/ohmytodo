package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.enums.Role;
import com.ohmy.todo.exception.UserAlreadyExistsException;
import com.ohmy.todo.exception.UserNotAuthorizedException;
import com.ohmy.todo.exception.UserNotFoundException;
import com.ohmy.todo.model.User;
import com.ohmy.todo.repository.UserRepository;
import com.ohmy.todo.service.UserService;
import com.ohmy.todo.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public List<UserDto> getAll() {
        log.debug("Fetching all users");
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDto)
                .toList();
    }

    @Override
    public boolean deleteBySecurityContextHolder() {
        User user = getUserBySecurityContextHolder();
        log.debug("Attempting to delete user: {}", user.getUsername());

        try {
            userRepository.delete(user);
            log.debug("User {} deleted successfully", user.getUsername());
            return true;
        } catch (Exception e) {
            log.warn("Unable to delete user {}", user.getUsername());
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.debug("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.debug("User with username '{}' not found", username);
                    return new UserNotFoundException(username);
                });

        log.debug("User '{}' found. Returning UserDetails", username);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    @Override
    public User getUserBySecurityContextHolder() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.debug("SecurityContext authentication: {}", auth.getName());

        if (!auth.isAuthenticated()) {
            log.warn("No authenticated user in SecurityContext");
            throw new UserNotAuthorizedException();
        }

        Object principal = auth.getPrincipal();
        log.debug("Authentication principal: {}", principal);

        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            log.debug("Extracted username from principal: {}", username);

            return userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.warn("User '{}' not found in database", username);
                        return new UserNotFoundException("User not found");
                    });
        }

        log.warn("Authentication principal is not of type UserDetails: {}", principal.getClass());
        throw new UserNotAuthorizedException();
    }

    private boolean doesUserExist(String username){
        return userRepository.findByUsername(username).isPresent();
    }
}
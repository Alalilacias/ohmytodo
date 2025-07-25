package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.enums.Role;
import com.ohmy.todo.exception.UserAlreadyExistsException;
import com.ohmy.todo.exception.UserDoesNotExist;
import com.ohmy.todo.model.Address;
import com.ohmy.todo.model.User;
import com.ohmy.todo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequest request;
    private User user;
    private List<User> users;

    @BeforeEach
    void setup() {
        Address address = new Address("Valencia 104", "Barcelona", "08015", "Cataluña, España");
        request = new UserRegistrationRequest("Manuel","Miranda","Password", address);
        user = User.builder()
                .id(1L) // Mocking the ID, as the test fails otherwise. Normally, database automatically adapts it.
                .name(request.name())
                .username(request.username())
                .password(request.password())
                .address(request.address())
                .role(Role.USER)
                .build();
        users = List.of(user, user, user);
    }

    @Test
    void testAddWhenNotExist() {
        when(userRepository.findByUsername("Miranda")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.add(request);

        assertEquals("Miranda", result.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAddWhenExists(){
        when(userRepository.findByUsername("Miranda")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.add(request));
    }

    @Test
    void testGetByIdWhenExists(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);
        assertEquals("Miranda", result.getUsername());
    }

    @Test
    void testGetByIdWhenNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExist.class, () -> userService.getById(99L));
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();
        assertEquals(3, result.size());
    }

    @Test
    void testDeleteWhenExists(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        boolean result = userService.delete(1L);
        assertTrue(result);
        verify(userRepository).deleteById(1L);
    }
    @Test
    void testDeleteWhenNotExists(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExist.class, () -> userService.delete(1L));
    }

    @Test
    void testLoadUserByUsernameWhenExists() {
        when(userRepository.findByUsername("Miranda")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("Miranda");

        assertEquals("Miranda", details.getUsername());
        assertEquals("Password", details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void testLoadUserByUsernameWhenNotExists() {
        when(userRepository.findByUsername("NotMiranda")).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExist.class, () -> userService.loadUserByUsername("NotMiranda"));
    }
}
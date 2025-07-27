package com.ohmy.todo.service.impl;

import com.ohmy.todo.dto.UserDto;
import com.ohmy.todo.dto.request.UserRegistrationRequest;
import com.ohmy.todo.enums.Role;
import com.ohmy.todo.exception.UserAlreadyExistsException;
import com.ohmy.todo.exception.UserDoesNotExistException;
import com.ohmy.todo.exception.UserNotAuthorizedException;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private SecurityContext securityContext;

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
        securityContext = mock(SecurityContext.class);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
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
    void testGetUserBySecurityContextHolderWhenAuthenticated(){
        when(userRepository.findByUsername("Miranda")).thenReturn(Optional.of(user));

        User result = userService.getUserBySecurityContextHolder();
        assertEquals("Miranda", result.getUsername());
    }

    @Test
    void testGetUserBySecurityContextHolderWhenNotAuthenticated() {
        SecurityContextHolder.clearContext();

        assertThrows(UserNotAuthorizedException.class, () -> userService.getUserBySecurityContextHolder());
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAll();
        assertEquals(3, result.size());
    }

    @Test
    void testDeleteBySecurityContextHolderWhenExists(){
        when(userRepository.findByUsername("Miranda")).thenReturn(Optional.of(user));
        when(userRepository.deleteByUsername("Miranda")).thenReturn(true);

        boolean result = userService.deleteBySecurityContextHolder();

        assertTrue(result);
        verify(userRepository).deleteByUsername("Miranda");
    }
    @Test
    void testDeleteBySecurityContextHolderWhenNotExists(){
        when(userRepository.findByUsername("Ghost")).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.deleteBySecurityContextHolder());
    }
    @Test
    void testDeleteBySecurityContextHolderWhenUnauthenticated() {
        SecurityContextHolder.clearContext();

        assertThrows(UserNotAuthorizedException.class, () -> userService.deleteBySecurityContextHolder());
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

        assertThrows(UserDoesNotExistException.class, () -> userService.loadUserByUsername("NotMiranda"));
    }
}
package com.ohmy.todo.model;

import static org.junit.jupiter.api.Assertions.*;

import com.ohmy.todo.enums.Role;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserBuilderAndGetters() {
        Address address = new Address("Baker St", "London", "NW1", "UK");

        User user = User.builder()
                .id(1L)
                .name("Yolanda Winston")
                .username("Captain Commodore Steele")
                .password("WinstonAndFergusonAreLovers")
                .address(address)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Yolanda Winston", user.getName());
        assertEquals("Captain Commodore Steele", user.getUsername());
        assertEquals("WinstonAndFergusonAreLovers", user.getPassword());
        assertNotNull(user.getAddress());
        assertEquals("London", user.getAddress().getCity());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getAddress());
    }

    @Test
    void testAllArgsConstructor() {
        Address address = new Address("Traction Ave 837", "Los Angeles", "90013", "US");
        User user = new User(1L, "Yolanda", "yolanda123", "secret", address, Role.USER);

        assertEquals("Yolanda", user.getName());
        assertEquals("yolanda123", user.getUsername());
        assertEquals("secret", user.getPassword());
        assertEquals("Traction Ave 837", user.getAddress().getStreet());
        assertEquals("Los Angeles", user.getAddress().getCity());
        assertEquals("90013", user.getAddress().getZipcode());
        assertEquals("US", user.getAddress().getCountry());
        assertNotEquals(Role.ADMIN, user.getRole());
    }
}
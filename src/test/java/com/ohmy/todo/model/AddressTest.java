package com.ohmy.todo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testBuilder() {
        Address address = Address.builder()
                .street("GV/Corts Catalanes 1558")
                .city("Barcelona")
                .zipcode("08001")
                .country("Spain")
                .build();

        assertEquals("GV/Corts Catalanes 1558", address.getStreet());
        assertEquals("Barcelona", address.getCity());
        assertEquals("08001", address.getZipcode());
        assertEquals("Spain", address.getCountry());
    }

    @Test
    void testAllArgsConstructor() {
        Address address = new Address("Carrer de la Marina 115", "Barcelona", "08013", "Spain");

        assertEquals("Carrer de la Marina 115", address.getStreet());
        assertEquals("Barcelona", address.getCity());
        assertEquals("08013", address.getZipcode());
        assertEquals("Spain", address.getCountry());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        Address address = new Address();

        address.setStreet("Av. Diagonal 284");
        address.setCity("Barcelona");
        address.setZipcode("08019");
        address.setCountry("Spain");

        assertEquals("Av. Diagonal 284", address.getStreet());
        assertEquals("Barcelona", address.getCity());
        assertEquals("08019", address.getZipcode());
        assertEquals("Spain", address.getCountry());
    }
}
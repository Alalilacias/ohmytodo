package com.ohmy.todo.repository;

import com.ohmy.todo.enums.Role;
import com.ohmy.todo.model.Address;
import com.ohmy.todo.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    final private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    @Test
    void testSaveAndFindUserWithEmbeddedAddress() {
        Address address = Address.builder()
                .street("/Corts Catalanes 235")
                .city("Barcelona")
                .zipcode("08950")
                .build();

        User user = User.builder()
                .name("Yolanda Winston")
                .username("Captain Commodore Steele")
                .password("hashed-password")
                .address(address)
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        Optional<User> found = userRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("Captain Commodore Steele");
        assertThat(found.get().getAddress().getCity()).isEqualTo("Barcelona");
    }
}
package com.formation.api.service;

import com.formation.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void shouldReturnAllUsers() {

        var users = userService.getAllUsers();

        assertThat(users)
                .hasSize(2)
                .extracting(User::getName)
                .containsExactly("Alice", "Bob");
    }

    @Test
    void shouldReturnUserWhenIdExists() {

        User user = userService.getUserById(1L);

        assertThat(user)
                .isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(1L);
                    assertThat(u.getName()).isEqualTo("Alice");
                    assertThat(u.getEmail()).isEqualTo("alice@test.com");
                });
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Utilisateur introuvable");
    }

    @Test
    void shouldCreateUser() {

        User user = new User(null, "Charlie", "charlie@test.com");

        User createdUser = userService.createUser(user);

        assertThat(createdUser)
                .isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(3L);
                    assertThat(u.getName()).isEqualTo("Charlie");
                    assertThat(u.getEmail()).isEqualTo("charlie@test.com");
                });

        assertThat(userService.getAllUsers())
                .hasSize(3)
                .extracting(User::getName)
                .contains("Charlie");
    }

    @Test
    void deleteUser() {

        userService.deleteUser(2L);

        assertThat(userService.getAllUsers())
                .hasSize(1)
                .extracting(User::getName)
                .contains("Alice");
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Utilisateur introuvable");
    }
}

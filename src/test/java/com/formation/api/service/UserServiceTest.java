package com.formation.api.service;

import com.formation.api.model.User;
import com.formation.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() {

        // GIVEN
        User user1 = new User();
        user1.setName("Alice");
        User user2 = new User();
        user2.setName("Bob");
        List<User> users = List.of(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        assertThat(result)
                .hasSize(2)
                .extracting(User::getName)
                .containsExactly("Alice", "Bob");
    }

    @Test
    void shouldReturnUserWhenIdExists() {

        // GIVEN
        User user = new User(1L, "Alice", "alice@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // WHEN
        User result = userService.getUserById(1L);

        // THEN
        assertThat(result)
                .isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(1L);
                    assertThat(u.getName()).isEqualTo("Alice");
                    assertThat(u.getEmail()).isEqualTo("alice@test.com");
                });
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // WHEN+THEN
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Utilisateur introuvable");
    }

    @Test
    void shouldCreateUser() {

        // GIVEN
        User user = new User(null, "Charlie", "charlie@test.com");
        User savedUser = new User(3L, "Charlie", "charlie@test.com");

        when(userRepository.save(user)).thenReturn(savedUser);

        // WHEN
        User result = userService.createUser(user);

        // THEN
        assertThat(result)
                .isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(3L);
                    assertThat(u.getName()).isEqualTo("Charlie");
                    assertThat(u.getEmail()).isEqualTo("charlie@test.com");
                });
    }

    @Test
    void shouldDeleteUser() {

        // GIVEN
        User user = new User(2L, "Bob", "bob@test.com");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        // WHEN
        userService.deleteUser(2L);

        // THEN
        verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        // WHEN+THEN
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Utilisateur introuvable");
    }
}

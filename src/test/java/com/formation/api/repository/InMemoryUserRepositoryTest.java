package com.formation.api.repository;

import com.formation.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserRepositoryTest {

    private InMemoryUserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserRepository();
    }

    @Test
    void findAll() {

        // WHEN
        List<User> users = repository.findAll();

        // THEN
        assertThat(users)
                .hasSize(2)
                .extracting(User::getName)
                .containsExactly("Alice", "Bob");
    }

    @Test
    void findById() {

        // WHEN
        Optional<User> user = repository.findById(1L);

        // THEN
        assertThat(user)
                .hasValueSatisfying(u -> {
                    assertThat(u.getId()).isEqualTo(1L);
                    assertThat(u.getName()).isEqualTo("Alice");
                    assertThat(u.getEmail()).isEqualTo("alice@test.com");
                });
    }

    @Test
    void findByIdShouldReturnEmptyWhenUserDoesNotExist() {

        // WHEN
        Optional<User> user = repository.findById(99L);

        // THEN
        assertThat(user).isEmpty();
    }

    @Test
    void save() {

        // GIVEN
        User user = new User(null, "Charlie", "charlie@test.com");

        // WHEN
        User saved = repository.save(user);

        // THEN
        assertThat(saved)
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(3L);
                    assertThat(u.getName()).isEqualTo("Charlie");
                    assertThat(u.getEmail()).isEqualTo("charlie@test.com");
                });

        List<User> users = repository.findAll();

        assertThat(users)
                .hasSize(3)
                .extracting(User::getName)
                .contains("Charlie");

    }

    @Test
    void saveShouldReuseDeletedId() {

        // GIVEN
        repository.delete(new User(2L, "Bob", "bob@test.com"));

        User charlie = new User(null, "Charlie", "charlie@test.com");

        // WHEN
        User saved = repository.save(charlie);

        // THEN
        assertThat(saved.getId()).isEqualTo(2L);
    }

    @Test
    void delete() {

        // GIVEN
        User user = new User(1L, "Alice", "alice@test.com");

        // WHEN
        repository.delete(user);

        // THEN
        assertThat(repository.findAll())
                .hasSize(1)
                .extracting(User::getName)
                .containsExactly("Bob");
    }

    @Test
    void deleteShouldReturnEmptyWhenUserDoesNotExist() {

        // GIVEN
        User user = new User(99L, "Charlie", "charlie@test.com");

        // WHEN
        repository.delete(user);

        // THEN
        assertThat(repository.findAll())
                .hasSize(2)
                .extracting(User::getName)
                .containsExactly("Alice", "Bob");
    }
}
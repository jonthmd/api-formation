package com.formation.api.repository;

import com.formation.api.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>(
            List.of(
                    new User(1L, "Alice", "alice@test.com"),
                    new User(2L, "Bob", "bob@test.com")
            )
    );

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public User save(User user) {

        long newId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;

        user.setId(newId);
        users.add(user);

        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }
}

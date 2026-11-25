package com.formation.api.service;

import com.formation.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

//    private final List<User> users = new ArrayList<>();
//
//    public UserService() {
//        resetUsers();
//    }
//
//    public void resetUsers() {
//        users.clear();
//
//        users.add(new User(1L, "Alice", "alice@test.com"));
//        users.add(new User(2L, "Bob", "bob@test.com"));
//    }

    private final List<User> users = new ArrayList<>(
            List.of(
                    new User(1L, "Alice", "alice@test.com"),
                    new User(2L, "Bob", "bob@test.com")
            )
    );

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Utilisateur introuvable"
                        )
                );
    }

    public User createUser(User user) {
        long newId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0) + 1;

        user.setId(newId);
        users.add(user);

        return user;
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        users.remove(user);
    }
}
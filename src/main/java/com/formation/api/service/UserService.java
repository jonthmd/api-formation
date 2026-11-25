package com.formation.api.service;

import com.formation.api.model.User;
import com.formation.api.repository.UserRepository;
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

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Utilisateur introuvable"
                        )
                );
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {

        User user = getUserById(id);
        userRepository.delete(user);
    }
}
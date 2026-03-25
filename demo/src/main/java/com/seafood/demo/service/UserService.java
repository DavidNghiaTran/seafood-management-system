package com.seafood.demo.service;

import com.seafood.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Login use case
    User authenticate(String username, String password);

    // Account management use cases
    List<User> getAllUsers();
    List<User> getUsersByRole(com.seafood.demo.entity.Role role);
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
}

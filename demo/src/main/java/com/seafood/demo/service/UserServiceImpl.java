package com.seafood.demo.service;

import com.seafood.demo.entity.User;
import com.seafood.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real application, you should hash passwords and compare hashes
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(com.seafood.demo.entity.Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Assuming username cannot be changed, or needs special handling
            // user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setFullName(userDetails.getFullName());
            user.setRole(userDetails.getRole());
            user.setPhone(userDetails.getPhone());
            user.setAddress(userDetails.getAddress());
            user.setPosition(userDetails.getPosition());
            return userRepository.save(user);
        }
        return null; // Or throw an exception
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

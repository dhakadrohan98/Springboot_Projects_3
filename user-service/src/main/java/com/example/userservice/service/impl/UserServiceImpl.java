package com.example.userservice.service.impl;

import com.example.userservice.entity.User;
import com.example.userservice.exception.DuplicateEmailException;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private User getExistingUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + userId
                        )
                );
    }

    @Override
    @Transactional
    public User createUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateEmailException(
                    "Email is already registered: " + user.getEmail()
            );
        }

        // Do not accept client-supplied ID or timestamps.
        user.setUserId(null);
        user.setCreatedAt(null);
        user.setUpdatedAt(null);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return getExistingUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User updateUser(Long userId, User userRequest) {

        User existingUser = getExistingUser(userId);

        if (userRepository.existsByEmailAndUserIdNot(
                userRequest.getEmail(), userId)) {
            throw new DuplicateEmailException(
                    "Email is already registered: " + userRequest.getEmail()
            );
        }

        // PUT replaces all editable fields.
        existingUser.setFirstName(userRequest.getFirstName());
        existingUser.setLastName(userRequest.getLastName());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhone(userRequest.getPhone());
        existingUser.setAddress(userRequest.getAddress());

        // JPA updates updatedAt through @PreUpdate.
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public User patchUser(Long userId, User userRequest) {

        User existingUser = getExistingUser(userId);

        if (userRequest.getEmail() != null) {

            if (userRequest.getEmail().isBlank() ||
                    !Pattern.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", userRequest.getEmail())) {
                throw new IllegalArgumentException(
                        "Please provide a valid email address"
                );
            }

            if (userRepository.existsByEmailAndUserIdNot(userRequest.getEmail(), userId)) {
                throw new DuplicateEmailException(
                        "Email is already registered: "
                                + userRequest.getEmail()
                );
            }

            existingUser.setEmail(userRequest.getEmail());
        }

        if (userRequest.getFirstName() != null) {
            if (userRequest.getFirstName().isBlank()) {
                throw new IllegalArgumentException(
                        "First name cannot be blank"
                );
            }
            existingUser.setFirstName(userRequest.getFirstName());
        }

        if (userRequest.getLastName() != null) {
            if (userRequest.getLastName().isBlank()) {
                throw new IllegalArgumentException(
                        "Last name cannot be blank"
                );
            }
            existingUser.setLastName(userRequest.getLastName());
        }

        if (userRequest.getPhone() != null) {
            existingUser.setPhone(userRequest.getPhone());
        }

        if (userRequest.getAddress() != null) {
            existingUser.setAddress(userRequest.getAddress());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User existingUser = getExistingUser(userId);
        userRepository.delete(existingUser);
    }
}

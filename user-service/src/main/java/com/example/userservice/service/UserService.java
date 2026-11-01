package com.example.userservice.service;

import com.example.userservice.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();

    User updateUser(Long userId, User user);

    User patchUser(Long userId, User user);

    void deleteUser(Long userId);
}

package com.example.userservice.controller;

import com.example.userservice.entity.User;

import com.example.userservice.service.UserService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create user
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        User createdUser = userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Fetch user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {

        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    // Fetch all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // Replace editable user fields
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {

        return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
    }

    // Update only the supplied fields
    @PatchMapping("/{userId}")
    public ResponseEntity<User> patchUser(@PathVariable Long userId, @RequestBody User user) {

        return new ResponseEntity<>(userService.patchUser(userId, user), HttpStatus.OK);
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
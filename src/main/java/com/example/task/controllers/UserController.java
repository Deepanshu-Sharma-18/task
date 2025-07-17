package com.example.task.controllers;

import com.example.task.dto.UserProfileRequestDTO;
import com.example.task.dto.UserResponseDTO;
import com.example.task.entity.Task;
import com.example.task.entity.User;
import com.example.task.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}/all")
    public ResponseEntity<Map<String, Object>> getUserAll(@PathVariable Long id) {
        log.info("Fetching full user info (with tasks) for user ID: {}", id);
        Optional<User> user = userService.getUserAll(id);

        Map<String, Object> body = new HashMap<>();
        body.put("status", user.isPresent() ? "Success" : "error");
        body.put("message", user.isPresent() ? "User found" : "User not found");
        body.put("code", user.isPresent() ? 200 : 404);
        body.put("user", user.orElse(null));

        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
        } else {
            log.info("User with ID {} retrieved successfully", id);
        }

        return new ResponseEntity<>(body, user.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        log.info("Fetching user basic details for ID: {}", id);
        Optional<UserResponseDTO> userDto = userService.getUserById(id);

        Map<String, Object> body = new HashMap<>();
        if (userDto.isEmpty()) {
            log.warn("User with ID {} not found", id);
            body.put("status", "error");
            body.put("message", "User not found");
            body.put("code", 404);
            body.put("user", null);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        log.info("User with ID {} found", id);
        body.put("status", "Success");
        body.put("message", "User found");
        body.put("code", 200);
        body.put("user", userDto.get());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userService.getAllUsers();

        log.info("Total users retrieved: {}", users.size());
        Map<String, Object> body = new HashMap<>();
        body.put("status", "Success");
        body.put("message", "");
        body.put("code", 200);
        body.put("user", users);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileRequestDTO request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setName(request.getName());
        userService.createUser(user);

        return ResponseEntity.ok("User profile updated successfully.");
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateUser(@Valid @RequestBody User user) {
        log.info("Updating user with ID: {}", user.getId());
        Optional<User> updatedUser = userService.updateUser(user);

        Map<String, Object> body = new HashMap<>();
        body.put("status", updatedUser.isPresent() ? "Success" : "error");
        body.put("message", updatedUser.isPresent() ? "User updated" : "User not updated");
        body.put("code", updatedUser.isPresent() ? 201 : 400);
        body.put("user", updatedUser.orElse(null));

        if (updatedUser.isPresent()) {
            log.info("User with ID {} updated successfully", user.getId());
        } else {
            log.warn("Failed to update user with ID {}", user.getId());
        }

        return new ResponseEntity<>(body, updatedUser.isPresent() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        boolean deleted = userService.deleteUser(id);

        Map<String, Object> body = new HashMap<>();
        body.put("status", deleted ? "Success" : "error");
        body.put("message", deleted ? "User deleted" : "User not deleted");
        body.put("code", deleted ? 200 : 400);
        body.put("user", null);

        if (deleted) {
            log.info("User with ID {} deleted successfully", id);
        } else {
            log.warn("User with ID {} could not be deleted", id);
        }

        return new ResponseEntity<>(body, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}


package com.example.task.services;

import com.example.task.dto.UserResponseDTO;
import com.example.task.entity.Task;
import com.example.task.entity.User;
import com.example.task.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> getUserAll(Long id) {
        log.info("Fetching complete user data (with tasks) for user ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
        }
        return user;
    }

    public Optional<UserResponseDTO> getUserById(Long id) {
        log.info("Fetching summary user data for user ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found", id);
            return Optional.empty();
        }

        User u = user.get();
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(u.getId());
        dto.setName(u.getName());
        dto.setTasksCount(u.getTasksCount());
        log.info("Returning DTO for user ID: {}", id);
        return Optional.of(dto);
    }

    public List<User> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Total users fetched: {}", users.size());
        return users;
    }

    public Optional<User> createUser(User user) {
        log.info("Creating user with name: {}", user.getName());
        userRepository.save(user);
        Optional<User> created = userRepository.findById(user.getId());

        if (created.isPresent()) {
            log.info("User created successfully with ID: {}", created.get().getId());
        } else {
            log.error("User creation failed for name: {}", user.getName());
        }

        return created;
    }

    public Optional<User> updateUser(User user) {
        log.info("Updating user with ID: {}", user.getId());
        Optional<User> existing = userRepository.findById(user.getId());

        if (existing.isEmpty()) {
            log.warn("User with ID {} not found. Cannot update.", user.getId());
            return Optional.empty();
        }

        User existingUser = existing.get();
        existingUser.setName(user.getName());
        existingUser.setTasks((ArrayList<Task>) user.getTasks());
        userRepository.save(existingUser);
        log.info("User with ID {} updated successfully", user.getId());

        return Optional.of(existingUser);
    }

    public boolean deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            userRepository.delete(user.get());
            log.info("User with ID {} deleted successfully", id);
            return true;
        } else {
            log.warn("User with ID {} not found. Delete aborted.", id);
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}


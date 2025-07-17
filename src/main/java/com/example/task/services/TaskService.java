package com.example.task.services;

import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;
import com.example.task.entity.User;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<TaskResponseDTO> getAllTasks(Long userId) {
        log.info("Fetching all tasks for user ID: {}", userId);
        List<Task> tasks = taskRepository.findByDateAndCompletedOptional(null, null, userId);
        log.info("Total tasks retrieved for user {}: {}", userId, tasks.size());
        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TaskResponseDTO> getTaskById(Long id) {
        log.info("Fetching task with ID: {}", id);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isEmpty()) {
            log.warn("Task with ID {} not found", id);
        } else {
            log.info("Task with ID {} found", id);
        }

        return task.map(this::toDTO);
    }

    public List<TaskResponseDTO> getFilteredTasks(Date date, Boolean completed, Long userId) {
        log.info("Filtering tasks for user ID {} with date={} and completed={}", userId, date, completed);
        List<Task> tasks = taskRepository.findByDateAndCompletedOptional(date, completed, userId);
        log.info("Total filtered tasks retrieved: {}", tasks.size());
        return tasks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<TaskResponseDTO> saveTask(Task task, Long userId) {
        log.info("Saving task for user ID: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            log.warn("User with ID {} not found. Cannot save task.", userId);
            return Optional.empty();
        }

        User u = user.get();
        u.setTasksCount(u.getTasksCount() + 1);
        userRepository.save(u);
        log.info("Updated tasksCount for user ID: {}", userId);

        task.setUser(u);
        Task newTask = taskRepository.save(task);
        log.info("Task saved successfully with ID: {}", newTask.getId());

        return Optional.of(toDTO(newTask));
    }

    public Optional<TaskResponseDTO> updateTask(Task task) {
        log.info("Updating task with ID: {}", task.getId());
        Optional<Task> existing = taskRepository.findById(task.getId());

        if (existing.isEmpty()) {
            log.warn("Task with ID {} not found. Update failed.", task.getId());
            return Optional.empty();
        }

        Task found = existing.get();
        found.setCompleted(task.isCompleted());
        found.setDate(task.getDate());
        found.setDescription(task.getDescription());
        found.setTitle(task.getTitle());

        Task updatedTask = taskRepository.save(found);
        log.info("Task with ID {} updated successfully", updatedTask.getId());

        return Optional.of(toDTO(updatedTask));
    }

    public boolean deleteTask(Long id) {
        log.info("Attempting to delete task with ID: {}", id);
        Optional<Task> task = taskRepository.findById(id);
        task.ifPresent(taskRepository::delete);

        if (task.isPresent()) {
            log.info("Task with ID {} deleted successfully", id);
        } else {
            log.warn("Task with ID {} not found. Deletion skipped.", id);
        }

        return task.isPresent();
    }

    private TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setCompleted(task.isCompleted());
        dto.setDate(task.getDate());
        dto.setDescription(task.getDescription());
        dto.setTitle(task.getTitle());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
        } else {
            log.warn("Task ID {} has no associated user", task.getId());
            dto.setUserId(null);
        }

        return dto;
    }
}

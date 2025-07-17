package com.example.task.controllers;

import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;
import com.example.task.services.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/all/{id}")
    ResponseEntity<Map<String, Object>> getAllTasks(@PathVariable Long id) {
        log.info("Fetching all tasks for user with ID: {}", id);
        Map<String, Object> body = new HashMap<>();
        body.put("status", "Success");
        body.put("message", "");
        body.put("code", 200);
        body.put("task", taskService.getAllTasks(id));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> getTask(@PathVariable Long id) {
        log.info("Fetching task with ID: {}", id);
        Map<String, Object> body = new HashMap<>();
        Optional<TaskResponseDTO> task = taskService.getTaskById(id);

        if (task.isEmpty()) {
            log.warn("Task with ID {} not found", id);
            body.put("status", "error");
            body.put("message", "Task not found");
            body.put("code", 404);
            body.put("task", null);
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        }

        log.info("Task with ID {} retrieved successfully", id);
        body.put("status", "Success");
        body.put("message", "Task found");
        body.put("code", 200);
        body.put("task", task.get());
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> getFilteredTasks(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam(required = true) Long userId,
            @RequestParam(required = false) Boolean completed) {

        log.info("Filtering tasks for user ID: {}, date: {}, completed: {}", userId, date, completed);
        Map<String, Object> body = new HashMap<>();
        body.put("status", "Success");
        body.put("message", "Tasks fetched");
        body.put("code", 200);
        body.put("task", taskService.getFilteredTasks(date, completed, userId));
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    ResponseEntity<Map<String, Object>> saveTask(@Valid @RequestBody Task task, @PathVariable Long id) {
        log.info("Creating new task for user ID: {}", id);
        Map<String, Object> body = new HashMap<>();
        Optional<TaskResponseDTO> saved = taskService.saveTask(task, id);

        if (saved.isEmpty()) {
            log.error("Failed to create task for user ID: {}", id);
            body.put("status", "Failure");
            body.put("message", "Task not created");
            body.put("code", 400);
            body.put("task", null);
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }

        log.info("Task created successfully for user ID: {}", id);
        body.put("status", "Success");
        body.put("message", "Task Created");
        body.put("code", 201);
        body.put("task", saved.get());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    ResponseEntity<Map<String, Object>> updateTask(@Valid @RequestBody Task task) {
        log.info("Updating task with ID: {}", task.getId());
        Map<String, Object> body = new HashMap<>();
        Optional<TaskResponseDTO> updated = taskService.updateTask(task);

        if (updated.isPresent()) {
            log.info("Task with ID {} updated successfully", task.getId());
        } else {
            log.warn("Failed to update task. Task with ID {} not found", task.getId());
        }

        body.put("status", updated.isPresent() ? "Success" : "error");
        body.put("message", updated.isPresent() ? "Task updated" : "Task not found");
        body.put("code", updated.isPresent() ? 200 : 404);
        body.put("task", updated.orElse(null));

        return new ResponseEntity<>(body, updated.isPresent() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        log.info("Deleting task with ID: {}", id);
        Map<String, Object> body = new HashMap<>();
        boolean deleted = taskService.deleteTask(id);

        if (deleted) {
            log.info("Task with ID {} deleted successfully", id);
        } else {
            log.warn("Failed to delete task. Task with ID {} not found", id);
        }

        body.put("status", deleted ? "Success" : "error");
        body.put("message", deleted ? "Task deleted" : "Task not deleted");
        body.put("code", deleted ? 200 : 400);
        body.put("task", null);

        return new ResponseEntity<>(body, deleted ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}

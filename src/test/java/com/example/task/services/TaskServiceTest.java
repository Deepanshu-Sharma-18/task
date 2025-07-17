package com.example.task.services;

import com.example.task.dto.TaskResponseDTO;
import com.example.task.entity.Task;
import com.example.task.entity.User;
import com.example.task.repository.TaskRepository;
import com.example.task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setUser(user);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setUser(user);

        List<Task> tasks = List.of(task1, task2);
        when(taskRepository.findByDateAndCompletedOptional(null,null,1L)).thenReturn(tasks);

        List<TaskResponseDTO> result = taskService.getAllTasks(1L);

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals(1L, result.get(0).getUserId());
    }


    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setUser(new User());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<TaskResponseDTO> result = taskService.getTaskById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void testSaveTask() {
        User user = new User();
        user.setId(1L);

        Task task = new Task();
        task.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Optional<TaskResponseDTO> result = taskService.saveTask(task, 1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testUpdateTask() {
        User user = new User();
        user.setId(100L);
        user.setName("Test User");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setUser(user);

        Task stored = new Task();
        stored.setId(1L);
        stored.setUser(user);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(stored));
        when(taskRepository.save(any(Task.class))).thenReturn(stored);

        Optional<TaskResponseDTO> result = taskService.updateTask(task);
        assertTrue(result.isPresent());
        assertEquals("Test Task", stored.getTitle());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        boolean result = taskService.deleteTask(1L);
        assertTrue(result);
        verify(taskRepository, times(1)).delete(task);
    }
}

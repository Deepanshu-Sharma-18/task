package com.example.task.services;



import com.example.task.dto.UserResponseDTO;
import com.example.task.entity.User;
import com.example.task.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserAll() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserAll(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setTasksCount(5);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponseDTO> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
        assertEquals(5, result.get().getTasksCount());
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.createUser(user);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Updated Name");

        User storedUser = new User();
        storedUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(storedUser));
        when(userRepository.save(storedUser)).thenReturn(storedUser);

        Optional<User> result = userService.updateUser(user);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", storedUser.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        boolean result = userService.deleteUser(1L);
        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }
}

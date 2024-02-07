package com.example.abtest.controller;

import com.example.abtest.dto.TaskDto;
import com.example.abtest.service.TaskService;
import com.example.abtest.web.controller.TaskController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllTasks() {
        List<TaskDto> expectedTasks = new ArrayList<>();
        expectedTasks.add(new TaskDto(1L, "Task 1", true));
        expectedTasks.add(new TaskDto(2L, "Task 2", false));
        when(taskService.getAll()).thenReturn(expectedTasks);
        List<TaskDto> result = taskController.getAllTasks();
        assertEquals(expectedTasks, result);
    }

    @Test
    void testGetTaskById() {
        Long taskId = 1L;
        TaskDto expectedTaskDto = new TaskDto(taskId, "Task 1", true);
        when(taskService.getById(taskId)).thenReturn(expectedTaskDto);
        ResponseEntity<TaskDto> result = taskController.getTaskById(taskId);
        assertEquals(expectedTaskDto, result.getBody());
    }

    @Test
    void testCreateTask() {
        TaskDto taskDto = new TaskDto(null, "New Task", false);
        TaskDto expectedTaskDto = new TaskDto(1L, "New Task", false);
        when(taskService.create(taskDto)).thenReturn(expectedTaskDto);
        ResponseEntity<TaskDto> result = taskController.createTask(taskDto);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(expectedTaskDto, result.getBody());
    }

    @Test
    void testUpdateTask() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task", true);
        TaskDto expectedTaskDto = new TaskDto(taskId, "Updated Task", true);
        when(taskService.update(taskId, taskDto)).thenReturn(expectedTaskDto);
        ResponseEntity<TaskDto> result = taskController.updateTask(taskId, taskDto);
        assertEquals(expectedTaskDto, result.getBody());
    }

    @Test
    void testDeleteTask() {
        Long taskId = 1L;
        ResponseEntity<?> result = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(taskService, times(1)).delete(taskId);
    }
}


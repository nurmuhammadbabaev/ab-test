package com.example.abtest.service;

import com.example.abtest.dto.TaskDto;
import com.example.abtest.dto.mapper.TaskMapper;
import com.example.abtest.entity.Task;
import com.example.abtest.exception.NotFoundException;
import com.example.abtest.repository.TaskRepository;
import com.example.abtest.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", true));
        tasks.add(new Task(2L, "Task 2", false));
        when(taskRepository.findAll()).thenReturn(tasks);
        List<TaskDto> expectedTaskDtos = new ArrayList<>();
        expectedTaskDtos.add(new TaskDto(1L, "Task 1", true));
        expectedTaskDtos.add(new TaskDto(2L, "Task 2", false));
        when(taskMapper.toDtoList(tasks)).thenReturn(expectedTaskDtos);
        List<TaskDto> result = taskService.getAll();
        assertEquals(expectedTaskDtos, result);
    }

    @Test
    void testGetById() {
        Long taskId = 1L;
        Task task = new Task(taskId, "Task 1", true);
        TaskDto expectedTaskDto = new TaskDto(taskId, "Task 1", true);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(expectedTaskDto);
        TaskDto result = taskService.getById(taskId);
        assertEquals(expectedTaskDto, result);
    }

    @Test
    void testGetById_NotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.getById(taskId));
    }

    @Test
    void testCreate() {
        TaskDto taskDto = new TaskDto(1L, "New Task", false);
        Task savedTask = new Task(1L, "New Task", false);
        when(taskMapper.toDto(any(Task.class))).thenAnswer(invocation -> {
            Task arg = invocation.getArgument(0);
            return new TaskDto(arg.getId(), arg.getDescription(), arg.isCompleted());
        });
        when(taskMapper.toDto(savedTask)).thenReturn(taskDto);
        when(taskRepository.saveAndFlush(any(Task.class))).thenReturn(savedTask);
        TaskDto result = taskService.create(taskDto);
        assertNotNull(result.getId());
        assertEquals(taskDto.getDescription(), result.getDescription());
        assertEquals(taskDto.isCompleted(), result.isCompleted());
    }

    @Test
    void testUpdate() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto(taskId, "Updated Task", true);
        Task existingTask = new Task(taskId, "Task", false);
        Task updatedTask = new Task(taskId, "Updated Task", true);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.saveAndFlush(existingTask)).thenReturn(updatedTask);
        when(taskMapper.toDto(any(Task.class))).thenAnswer(invocation -> {
            Task arg = invocation.getArgument(0);
            return new TaskDto(arg.getId(), arg.getDescription(), arg.isCompleted());
        });
        TaskDto result = taskService.update(taskId, taskDto);
        assertEquals(taskDto, result);
    }

    @Test
    void testDelete_NotFound() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> taskService.delete(taskId));
    }
}


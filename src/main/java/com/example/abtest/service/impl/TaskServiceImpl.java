package com.example.abtest.service.impl;

import com.example.abtest.dto.TaskDto;
import com.example.abtest.dto.mapper.TaskMapper;
import com.example.abtest.entity.Task;
import com.example.abtest.exception.NotFoundException;
import com.example.abtest.repository.TaskRepository;
import com.example.abtest.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper taskMapper;

    @Override
    public List<TaskDto> getAll() {
        return taskMapper.toDtoList(repository.findAll());
    }

    @Override
    public TaskDto getById(Long id) {
        return taskMapper.toDto(findById(id));
    }

    @Override
    public TaskDto create(TaskDto task) {
        return taskMapper.toDto(save(new Task(), task));
    }

    @Override
    public TaskDto update(Long id, TaskDto task) {
        return taskMapper.toDto(save(findById(id), task));
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    private Task save(Task task, TaskDto request) {
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        return repository.saveAndFlush(task);
    }

    private Task findById(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Table Task with id = {} does not exists", id);
            throw new NotFoundException(String.format("Table Task with id = %s does not exists", id), HttpStatus.NOT_FOUND);
        });
    }
}

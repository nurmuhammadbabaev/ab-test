package com.example.abtest.service;

import com.example.abtest.dto.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getAll();

    TaskDto getById(Long id);

    TaskDto create(TaskDto task);

    TaskDto update(Long id, TaskDto task);

    void delete(Long id);

}

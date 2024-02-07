package com.example.abtest.dto.mapper;

import com.example.abtest.dto.TaskDto;
import com.example.abtest.entity.Task;

import java.util.List;

public interface TaskMapper {

    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> taskList);

}

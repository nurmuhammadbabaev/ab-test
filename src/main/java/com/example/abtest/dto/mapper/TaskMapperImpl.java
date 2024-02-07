package com.example.abtest.dto.mapper;

import com.example.abtest.dto.TaskDto;
import com.example.abtest.entity.Task;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setCompleted(task.isCompleted());
        return dto;
    }

    @Override
    public List<TaskDto> toDtoList(List<Task> taskList) {
        List<TaskDto> taskDtoList=new ArrayList<>();
        for (Task task:taskList) {
            taskDtoList.add(toDto(task));
        }
        return taskDtoList;
    }
}

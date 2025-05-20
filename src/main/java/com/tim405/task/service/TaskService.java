package com.tim405.task.service;

import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.dto.TaskResponseDTO;
import com.tim405.task.entity.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO request);

    TaskResponseDTO findTaskById(Long taskId);

    TaskResponseDTO updateTask(Long taskId, TaskRequestDTO request);

    void deleteTaskById(Long taskId);

    List<TaskResponseDTO> getAllTasks();

    TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus newStatus);
}
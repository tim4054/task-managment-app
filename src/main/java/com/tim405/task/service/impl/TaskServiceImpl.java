package com.tim405.task.service.impl;

import com.tim405.task.aspect.annotations.LogExecutionTime;
import com.tim405.task.aspect.annotations.LogResult;
import com.tim405.task.aspect.annotations.Loggable;
import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.dto.TaskResponseDTO;
import com.tim405.task.entity.Task;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.exception.TaskNotFoundException;
import com.tim405.task.repository.TaskRepository;
import com.tim405.task.service.TaskService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TaskServiceImpl(TaskRepository taskRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Loggable
    @LogExecutionTime
    @LogResult
    public TaskResponseDTO createTask(TaskRequestDTO request) {
        Task task = new Task(request.getTitle(), request.getDescription(), request.getUserId());
        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Override
    @Loggable
    @LogExecutionTime
    @LogResult
    public TaskResponseDTO findTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return mapToResponse(task);
    }

    @Override
    @Loggable
    @LogExecutionTime
    @LogResult
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(request.getUserId());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    @Override
    @Loggable
    @LogExecutionTime
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    @Loggable
    @LogResult
    public List<TaskResponseDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TaskResponseDTO mapToResponse(Task task) {
        TaskResponseDTO response = new TaskResponseDTO();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setUserId(task.getUserId());
        response.setStatus(task.getTaskStatus());
        return response;
    }

    @Override
    @Loggable
    @LogExecutionTime
    @LogResult
    public TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setTaskStatus(newStatus);

        Task updatedTask = taskRepository.save(task);

        kafkaTemplate.send(
                "task-updates",
                taskId.toString(),
                newStatus.name()
        );

        return mapToResponse(updatedTask);
    }
}
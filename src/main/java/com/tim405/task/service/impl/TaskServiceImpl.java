package com.tim405.task.service.impl;

import com.tim405.task.entity.Task;
import com.tim405.task.repository.TaskRepository;
import com.tim405.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    public TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(new Task(task.getTitle(),
                task.getDescription(),
                task.getUserId()));
    }

    @Override
    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow();
    }

    @Override
    public Task updateTask(Long taskId, Task task) {
        Task updatedTask = new Task(task.getTitle(),
                task.getDescription(),
                task.getUserId());
        updatedTask.setId(taskId);
        return taskRepository.save(updatedTask);
    }

    @Override
    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}

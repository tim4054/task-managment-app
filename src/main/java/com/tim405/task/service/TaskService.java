package com.tim405.task.service;

import com.tim405.task.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);

    Task findTaskById(Long taskId);

    Task updateTask(Long taskId, Task task);

    void deleteTaskById(Long taskId);

    List<Task> getAllTasks();
}

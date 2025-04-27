package com.tim405.task.controller;

import com.tim405.task.entity.Task;
import com.tim405.task.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    public TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping()
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping("{id}")
    public Task findTaskById(@PathVariable Long taskId) {
        return taskService.findTaskById(taskId);
    }

    @PutMapping("{id}")
    public Task updateTask(@RequestParam Long taskId,
                           @RequestBody Task task) {
        return taskService.updateTask(taskId, task);
    }

    @DeleteMapping("{id}")
    public void deleteTaskById(Long taskId) {
        taskService.deleteTaskById(taskId);
    }

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAllTasks();
    }
}

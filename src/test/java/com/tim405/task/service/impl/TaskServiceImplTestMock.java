package com.tim405.task.service.impl;

import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.dto.TaskResponseDTO;
import com.tim405.task.entity.Task;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.exception.TaskNotFoundException;
import com.tim405.task.repository.TaskRepository;
import com.tim405.task.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTestMock {
    @Mock
    private TaskRepository taskRepositoryMock;
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    @DisplayName("Тест успешного создания задачи")
    void createTask() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle("testTask");
        taskRequestDTO.setDescription("new task");
        taskRequestDTO.setUserId(1L);
        taskRequestDTO.setStatus(TaskStatus.NEW);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("testTask");
        savedTask.setDescription("new task");
        savedTask.setUserId(1L);
        savedTask.setTaskStatus(TaskStatus.NEW);

        when(taskRepositoryMock.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO actual = taskService.createTask(taskRequestDTO);

        assertNotNull(actual);
        assertEquals(savedTask.getId(), actual.getId());
        assertEquals(savedTask.getTitle(), actual.getTitle());
        assertEquals(savedTask.getDescription(), actual.getDescription());
        assertEquals(savedTask.getUserId(), actual.getUserId());
        assertEquals(savedTask.getTaskStatus(), actual.getStatus());
    }

    @Test
    @DisplayName("Тест успешного поиска задачи")
    void findTaskById() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("testTask");
        savedTask.setDescription("new task");
        savedTask.setUserId(1L);
        savedTask.setTaskStatus(TaskStatus.NEW);

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(savedTask));

        TaskResponseDTO actual = taskService.findTaskById(1L);

        assertNotNull(actual);
        assertEquals(savedTask.getId(), actual.getId());
        assertEquals(savedTask.getTitle(), actual.getTitle());
        assertEquals(savedTask.getDescription(), actual.getDescription());
        assertEquals(savedTask.getUserId(), actual.getUserId());
        assertEquals(savedTask.getTaskStatus(), actual.getStatus());
    }

    @Test
    @DisplayName("Тест неуспешного поиска задачи")
    void findTaskByIdShouldThrowsNotFoundException() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.findTaskById(1L);
        });
    }


    @Test
    void updateTask() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        Task existingTask = new Task("old title", "old desc", 1L);
        existingTask.setId(1L);

        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("new title");
        requestDTO.setDescription("new desc");
        requestDTO.setUserId(2L);

        Task updatedTask = new Task("new title", "new desc", 2L);
        updatedTask.setId(1L);

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepositoryMock.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponseDTO responseDTO = taskService.updateTask(1L, requestDTO);

        assertEquals("new title", responseDTO.getTitle());
        assertEquals("new desc", responseDTO.getDescription());
        assertEquals(2L, responseDTO.getUserId());
    }

    @Test
    void deleteTaskById() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        doNothing().when(taskRepositoryMock).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTaskById(1L));
        verify(taskRepositoryMock, times(1)).deleteById(1L);
    }

    @Test
    void getAllTasks() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        Task task = new Task("Title", "Desc", 1L);
        task.setId(1L);
        task.setTaskStatus(TaskStatus.NEW);

        when(taskRepositoryMock.findAll()).thenReturn(Collections.singletonList(task));

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Title", result.get(0).getTitle());
    }

    @Test
    void updateTaskStatus() {
        TaskService taskService = new TaskServiceImpl(taskRepositoryMock, kafkaTemplate);

        Task existingTask = new Task("Task", "Desc", 1L);
        existingTask.setId(1L);
        existingTask.setTaskStatus(TaskStatus.NEW);

        Task updatedTask = new Task("Task", "Desc", 1L);
        updatedTask.setId(1L);
        updatedTask.setTaskStatus(TaskStatus.COMPLETED);

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepositoryMock.save(any(Task.class))).thenReturn(updatedTask);

        TaskResponseDTO result = taskService.updateTaskStatus(1L, TaskStatus.COMPLETED);

        assertEquals(TaskStatus.COMPLETED, result.getStatus());
        verify(kafkaTemplate, times(1)).send("task-updates", "1", "COMPLETED");
    }
}
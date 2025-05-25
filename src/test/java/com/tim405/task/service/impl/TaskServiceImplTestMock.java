package com.tim405.task.service.impl;

import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.dto.TaskResponseDTO;
import com.tim405.task.entity.Task;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.exception.TaskNotFoundException;
import com.tim405.task.repository.TaskRepository;
import com.tim405.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void deleteTaskById() {
    }

    @Test
    void getAllTasks() {
    }

    @Test
    void updateTaskStatus() {
    }
}
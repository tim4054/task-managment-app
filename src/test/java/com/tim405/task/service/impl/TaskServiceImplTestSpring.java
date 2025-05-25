package com.tim405.task.service.impl;

import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.dto.TaskResponseDTO;
import com.tim405.task.entity.Task;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.repository.TaskRepository;
import com.tim405.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest()
class TaskServiceImplTestSpring extends AbstractContainerBaseTest{

    @Autowired
    private TaskServiceImpl taskService;

    @Autowired
    private TaskRepository taskRepository;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Тест успешного создания задачи")
    void createTask() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTitle("testTask");
        taskRequestDTO.setDescription("new task");
        taskRequestDTO.setUserId(1L);
        taskRequestDTO.setStatus(TaskStatus.NEW);

        TaskResponseDTO actual = taskService.createTask(taskRequestDTO);

        assertNotNull(actual);
        assertEquals(actual.getId(), 1L);
        assertEquals(taskRequestDTO.getTitle(), actual.getTitle());
        assertEquals(taskRequestDTO.getDescription(), actual.getDescription());
        assertEquals(taskRequestDTO.getUserId(), actual.getUserId());
        assertEquals(TaskStatus.NEW, actual.getStatus());
    }

    @Test
    void findTaskById() {
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
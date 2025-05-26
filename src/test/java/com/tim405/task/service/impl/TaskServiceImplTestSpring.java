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

import java.util.List;
import java.util.Optional;

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
    @DisplayName("Тест успешного поиска задачи по ID")
    void findTaskById() {
        Task task = new Task("Find me", "Please", 2L);
        task.setTaskStatus(TaskStatus.NEW);
        task = taskRepository.save(task);

        TaskResponseDTO found = taskService.findTaskById(task.getId());

        assertNotNull(found);
        assertEquals(task.getId(), found.getId());
        assertEquals("Find me", found.getTitle());
        assertEquals("Please", found.getDescription());
        assertEquals(2L, found.getUserId());
        assertEquals(TaskStatus.NEW, found.getStatus());
    }

    @Test
    @DisplayName("Тест успешного обновления задачи")
    void updateTask() {
        Task task = new Task("Old title", "Old desc", 3L);
        task.setTaskStatus(TaskStatus.NEW);
        task = taskRepository.save(task);

        TaskRequestDTO updateDTO = new TaskRequestDTO();
        updateDTO.setTitle("Updated title");
        updateDTO.setDescription("Updated desc");
        updateDTO.setUserId(4L);

        TaskResponseDTO updated = taskService.updateTask(task.getId(), updateDTO);

        assertEquals("Updated title", updated.getTitle());
        assertEquals("Updated desc", updated.getDescription());
        assertEquals(4L, updated.getUserId());
    }

    @Test
    @DisplayName("Тест успешного удаления задачи")
    void deleteTaskById() {
        Task task = new Task("Delete me", "Now", 5L);
        task.setTaskStatus(TaskStatus.NEW);
        task = taskRepository.save(task);

        Task finalTask = task;
        assertDoesNotThrow(() -> taskService.deleteTaskById(finalTask.getId()));

        Optional<Task> deleted = taskRepository.findById(task.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    @DisplayName("Тест получения всех задач")
    void getAllTasks() {
        Task task1 = new Task("Task1", "Desc1", 1L);
        Task task2 = new Task("Task2", "Desc2", 2L);
        taskRepository.save(task1);
        taskRepository.save(task2);

        List<TaskResponseDTO> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        boolean containsTask1 = result.stream().anyMatch(t -> t.getTitle().equals("Task1"));
        boolean containsTask2 = result.stream().anyMatch(t -> t.getTitle().equals("Task2"));
        assertTrue(containsTask1);
        assertTrue(containsTask2);
    }

    @Test
    @DisplayName("Тест обновления статуса задачи")
    void updateTaskStatus() {
        Task task = new Task("Status update", "Change my status", 6L);
        task.setTaskStatus(TaskStatus.NEW);
        task = taskRepository.save(task);

        TaskResponseDTO result = taskService.updateTaskStatus(task.getId(), TaskStatus.IN_PROGRESS);

        assertEquals(TaskStatus.IN_PROGRESS, result.getStatus());
    }

}
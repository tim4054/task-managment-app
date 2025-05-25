package com.tim405.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTestMockMvc {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask() throws Exception {
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("Test Task");
        requestDTO.setDescription("Test Description");
        requestDTO.setUserId(1L);
        requestDTO.setStatus(TaskStatus.NEW);

        String jsonRequest = objectMapper.writeValueAsString(requestDTO);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Matchers.equalTo(TaskStatus.NEW.name())));

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
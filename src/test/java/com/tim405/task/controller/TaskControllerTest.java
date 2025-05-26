package com.tim405.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tim405.task.dto.TaskRequestDTO;
import com.tim405.task.entity.TaskStatus;
import com.tim405.task.service.impl.AbstractContainerBaseTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TaskControllerTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long createTestTaskAndGetId() throws Exception {
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("Task for ID");
        requestDTO.setDescription("Desc");
        requestDTO.setUserId(1L);
        requestDTO.setStatus(TaskStatus.NEW);

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void createTask() throws Exception {
        TaskRequestDTO requestDTO = new TaskRequestDTO();
        requestDTO.setTitle("Test Task");
        requestDTO.setDescription("Test Description");
        requestDTO.setUserId(1L);
        requestDTO.setStatus(TaskStatus.NEW);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.status").value(TaskStatus.NEW.name()));
    }

    @Test
    void findTaskById() throws Exception {
        Long id = createTestTaskAndGetId();

        mockMvc.perform(get("/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void updateTask() throws Exception {
        Long id = createTestTaskAndGetId();

        TaskRequestDTO updateRequest = new TaskRequestDTO();
        updateRequest.setTitle("Updated Title");
        updateRequest.setDescription("Updated Desc");
        updateRequest.setUserId(1L);
        updateRequest.setStatus(TaskStatus.IN_PROGRESS); // можно опустить, если не используется

        mockMvc.perform(put("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Desc"));
    }

    @Test
    void deleteTaskById() throws Exception {
        Long id = createTestTaskAndGetId();

        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTasks() throws Exception {
        createTestTaskAndGetId();
        createTestTaskAndGetId();

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(Matchers.greaterThanOrEqualTo(2))));
    }

    @Test
    void updateTaskStatus() throws Exception {
        Long id = createTestTaskAndGetId();

        mockMvc.perform(put("/tasks/set-status/{id}?taskStatus=COMPLETED", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

}

package com.TaskDemo.Task;

import com.TaskDemo.Entity.Task;
import com.TaskDemo.Repository.TaskRepository;
import com.TaskDemo.controller.TaskController;
import com.TaskDemo.service.taskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTests {

    private MockMvc mockMvc;

    @Mock
    private taskService taskservice;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    Task task1 = new Task(1L, "Task1", "Task1 added", (short) 2, new Date(), (short) 4);
    Task task2 = new Task(2L, "Task2", "Task2 added", (short) 2, new Date(), (short) 4);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void getAllTaskTest_success() throws Exception {
        List<Task> taskList = new ArrayList<>(Arrays.asList(task1, task2));

        // Mocking behavior of taskService
        when(taskservice.findAllTasks()).thenReturn(taskList);

        // Mocking behavior of taskRepository
        when(taskRepository.findAllTasks()).thenReturn(taskList);

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
}

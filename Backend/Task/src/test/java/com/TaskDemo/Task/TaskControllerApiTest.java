package com.TaskDemo.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.TaskDemo.Entity.Task;
import com.TaskDemo.Repository.TaskRepository;
import com.TaskDemo.controller.TaskController;
import com.TaskDemo.service.taskService;
import com.TaskDemo.serviceImpl.taskServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.ObjectWriter;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class) 
@WebMvcTest(TaskController.class)
@Import(TestDataSourceConfig.class)
public class TaskControllerApiTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter =objectMapper.writer();

    @Mock
    private taskService taskservice;

    @InjectMocks
    private TaskController taskController;

    Task task1 = new Task(1L,"Task1","Task1 added",(short)2,new Date(),(short)4);
    Task task2 = new Task(2L,"Task2","Task2 added",(short)2,new Date(),(short)4);
    Task task3 = new Task(3L,"Task3","Task3 added",(short)2,new Date(),(short)4);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

    }

    @Test
    public void getAllTaskTest_success()  throws Exception{
        List<Task> taskList = new ArrayList<>(Arrays.asList(task1,task2,task3));

		when(taskservice.findAllTasks()).thenReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)));
    }
    
    @Test
    public void getTaskById_success() throws Exception{
    	when(taskservice.getTaskById(task1.getId())).thenReturn(task1);
    	
    	mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskName", is("Task1")));
    
    }
    
    @Test
    public void getTaskById_notFound() throws Exception{
    	when(taskservice.getTaskById(task1.getId())).thenReturn(null);
    	
    	mockMvc.perform(MockMvcRequestBuilders
                .get("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());                
    
    }
    
    @Test
    public void createNewTask_success() throws Exception{
        Task task4 = new Task(4L,"Task4","Task4 added",(short)2,new Date(),(short)4);

        when(taskservice.createTask(any(Task.class))).thenReturn(4L);
        
        String content =  objectWriter.writeValueAsString(task4);
        
        
        
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
        		.post("/tasks")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(content); 
        
        mockMvc.perform(mockRequest)
        		.andExpect(status().isCreated());
        
    }
    
    @Test
    public void updateTask_success() throws Exception{
    	
        Task task4 = new Task(4L,"Task4","Task4 added",(short)2,new Date(),(short)4);
    
    	task4.setTaskName("Task4 Update");
    	when(taskservice.updateTask(eq(4L), any(Task.class))).thenReturn(true);
    	
        String content =  objectWriter.writeValueAsString(task4);
        
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
        		.put("/tasks/4")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON)
        		.content(content);

        mockMvc.perform(mockRequest)
		.andExpect(status().isOk());
    	
    }
    
    
}

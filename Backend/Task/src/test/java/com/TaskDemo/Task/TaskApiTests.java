package com.TaskDemo.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.TaskDemo.Entity.Task;
import com.TaskDemo.Repository.TaskRepository;
import com.TaskDemo.controller.TaskController;
import com.TaskDemo.service.taskService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Hidden;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@AutoConfigureMockMvc
@SpringBootTest
public class TaskApiTests {
	
	@Autowired 
	private MockMvc mockMvc;


	@Autowired
    private TaskRepository taskRepository;
	
	@Autowired
    private ObjectMapper objectMapper;

	
	
	private static final String END_POINT_URL = "/tasks";
	
	 @BeforeEach
	    void setUp() {
	        // Initialize mockMvc before each test method
	    }
	@Test
	public void textCreateNewTaskAPI() throws Exception {
		Task task = new Task(1L,"Task1","Working",(short)2,new Date(),(short)5);
		mockMvc.perform(post(END_POINT_URL)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isCreated()).andDo(print());		
	}
	
	@Test
	public void testCreateNewTaskWithBadRequest() throws Exception{
		Task task = new Task(1L,"Task 22","Working",(short)2,new Date(),null);
		mockMvc.perform(post(END_POINT_URL)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(task)))
				.andExpect(status().isBadRequest()).andDo(print());
		
	}
	
	@Test
	public void test_getTaskFromId() throws Exception{
		Long taskId = 4L;
		ResultActions response=mockMvc.perform(get("/tasks/{id}", taskId))
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void getAllTasks() throws Exception{
		
	}
	
	
	
	
	
	
}

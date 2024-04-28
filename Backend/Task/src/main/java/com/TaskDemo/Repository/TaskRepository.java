package com.TaskDemo.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.TaskDemo.Entity.Task;
import com.TaskDemo.serviceImpl.RowMapperImpl;

@Repository
public class TaskRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
	
	
	public TaskRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Task findById(Long id) {
		String sql = "SELECT * FROM tasks WHERE id = ?";
		RowMapper<Task> mapper = new RowMapperImpl();
		Task currTask;
		try {
			currTask = this.jdbcTemplate.queryForObject(sql, mapper,id);	
		}catch(Exception ex) {
			return null;
		}
		return currTask;		
	}
	
	public List<Task> findAllTasks(){
		String sql= "SELECT * FROM tasks";
		List<Task> totalTasks = this.jdbcTemplate.query(sql, new RowMapperImpl());
		return totalTasks;
	}
	
	public Long save(Task task) {
		String sql = "INSERT INTO tasks(taskName, taskDescription, taskStatus, dateSubmit, priority) VALUES (?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {
	        @Override
	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	            // Create a PreparedStatement with the SQL query and specify the generated key column names
	            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"}); // Specify the column names for generated keys

	            // Set the parameters for the PreparedStatement
	            ps.setString(1, task.getTaskName()); 
	            ps.setString(2, task.getTaskDescription());  
	            
	            ps.setShort(3, (short) 0);
	            
	            
	            java.util.Date utilDate = task.getDateSubmit();
	            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

	            ps.setDate(4, sqlDate);
	            ps.setShort(5, task.getPriority());
	            // Return the configured PreparedStatement
	            return ps;
	        }
	    }, keyHolder);
//        Long id = (long) jdbcTemplate.update("INSERT INTO tasks(taskName, taskDescription, taskStatus, dateSubmit, priority) VALUES (?, ?, ?, ?, ?)",
//                task.getTaskName(), task.getTaskDescription(), task.getStatus(), task.getDateSubmit(), task.getPriority());
//        task.setId(id);
		if(keyHolder!=null && keyHolder.getKey()!=null) {
			task.setId(keyHolder.getKey().longValue());
		}
		return keyHolder.getKey().longValue();
    }

    public boolean update(Task task) {
       int rowsUpdate =  this.jdbcTemplate.update("UPDATE tasks SET taskName=?, taskDescription=?, taskStatus=?, dateSubmit=?, priority=? WHERE id=?",
                task.getTaskName(), task.getTaskDescription(), task.getStatus(), task.getDateSubmit(), task.getPriority(), task.getId());
       return rowsUpdate>0;
    }

    public boolean deleteById(Long id) {
        int rowsUpdate =  this.jdbcTemplate.update("DELETE FROM tasks WHERE id=?", id);
        return rowsUpdate > 0;
    }

}

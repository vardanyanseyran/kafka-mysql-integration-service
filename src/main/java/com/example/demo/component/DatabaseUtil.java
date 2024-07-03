package com.example.demo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseUtil {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveMessage(String message) {
        String insertSQL = "INSERT INTO messages (content) VALUES (?)";
        jdbcTemplate.update(insertSQL, message);
    }
}
package com.todolist.DoToday.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class AddRequestService {
    private JdbcTemplate jdbcTemplate;

    public AddRequestService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}

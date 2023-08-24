package com.todolist.DoToday.service;

import com.todo.todolist.dto.response.TestDto;
import com.todo.todolist.entity.Test;

public interface FirebaseService {

    public String insert(TestDto testDto) throws Exception;

    public Test getDetail(String id) throws Exception;

    public String update(TestDto testDto) throws Exception;

    public String delete(String id) throws Exception;
}

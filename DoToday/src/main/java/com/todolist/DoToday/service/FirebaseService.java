package com.todolist.DoToday.service;

import com.todolist.DoToday.entity.Test;

public interface FirebaseService {

    public String insert(Test test) throws Exception;

    public Test getDetail(String id) throws Exception;

    public String update(Test test) throws Exception;

    public String delete(String id) throws Exception;
}

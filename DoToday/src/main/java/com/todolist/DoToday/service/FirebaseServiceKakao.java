package com.todolist.DoToday.service;

import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.entity.Test;

public interface FirebaseServiceKakao {

    public String insert(Members members) throws Exception;

    public Members getDetail(String id) throws Exception;

    public String update(Members members) throws Exception;

    public String delete(String id) throws Exception;
}

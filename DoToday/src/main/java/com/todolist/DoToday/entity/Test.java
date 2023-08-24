package com.todolist.DoToday.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Test {

    private String id;
    private String name;
    private String pw;
    private String email;

    public Test(String id, String name, String pw, String email) {
        this.id = id;
        this.name = name;
        this.pw = pw;
        this.email = email;
    }
}

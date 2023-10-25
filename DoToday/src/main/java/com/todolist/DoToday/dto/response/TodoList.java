package com.todolist.DoToday.dto.response;

import lombok.Data;

@Data
public class TodoList {
    private int listNum;
    private String listTitle;
    private String memberId;
    private int complete;
    private String date;
    private String whenToDo;
}

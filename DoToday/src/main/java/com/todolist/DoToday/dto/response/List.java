package com.todolist.DoToday.dto.response;

import lombok.Data;
import lombok.Getter;

@Data
public class List {
    private int listNum;
    private String listTitle;
    private String memberId;
    private int complete;
}

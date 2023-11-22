package com.todolist.DoToday.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppListGetDto {
    private int listNum;
    private String listTitle;
    private String memberId;
    private String date;
    private String whenToDo;
    private int status;
}

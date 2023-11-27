package com.todolist.DoToday.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppListGetDto { // 받아서 작성, 수정용 dto
    private int listNum;
    private String listTitle;
    private String memberId;
    private String date;
    private String whenToDo;
    private int complete;
    private String status;//작성인지 수정인지 구분
}

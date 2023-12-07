package com.todolist.DoToday.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppListGetDto { // 받아서 작성, 수정용 dto
    private int listNum;
    private String listTitle;
//    private String accessToken;
    private String date;
    private String whenToDo;
    private int complete;
    private String type;//작성인지 수정인지 구분
}

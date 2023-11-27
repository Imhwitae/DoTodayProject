package com.todolist.DoToday.api.reponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppListDto { //리스트 내용을 담아서 보낼 dto
    private int listNum;
    private String listTitle;
//    private String memberId;
    private int complete;
//    private String date;
    private String whenToDo;
}

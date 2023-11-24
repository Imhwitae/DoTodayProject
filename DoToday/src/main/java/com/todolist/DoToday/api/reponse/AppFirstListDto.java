package com.todolist.DoToday.api.reponse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AppFirstListDto {
    private List<AppListDto> list;
    private String token;
    private String date;
}

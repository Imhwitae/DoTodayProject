package com.todolist.DoToday.api.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GiveTokenAndNum {
    private Long member_num;
    private String access_token;
    private String refresh_token;
}

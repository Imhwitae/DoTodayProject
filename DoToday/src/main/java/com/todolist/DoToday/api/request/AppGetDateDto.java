package com.todolist.DoToday.api.request;

import lombok.Getter;

@Getter
public class AppGetDateDto { //특정 날짜에 저장된 리스트를 받아올때 쓸 날짜와 토큰(유저아이디) 값을 받아올 dto
    private String accessToken;
    private String date;
}

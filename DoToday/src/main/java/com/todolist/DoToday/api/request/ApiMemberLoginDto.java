package com.todolist.DoToday.api.request;

import lombok.Getter;

@Getter
public class ApiMemberLoginDto {
    private String memberId;
    private String memberPw;
}

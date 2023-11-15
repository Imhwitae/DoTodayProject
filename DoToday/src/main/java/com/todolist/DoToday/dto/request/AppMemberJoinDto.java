package com.todolist.DoToday.dto.request;

import lombok.Getter;

@Getter
public class AppMemberJoinDto {
    private String registration_id;  // kakao, naver, google...
    private String id;  // email = id
    private String pw;  // uid = pw
    private String name;
    private String image_url;
    private String gender;
}

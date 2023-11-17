package com.todolist.DoToday.api.request;

import lombok.Getter;

@Getter
public class ApiMemberJoinDto {
    private String registration_id;  // kakao, naver, google...
    private String id;  // email = id
    private String pw;  // uid = pw
    private String name;
    private String image_url;
//    private String gender;
}

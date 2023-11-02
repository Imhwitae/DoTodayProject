package com.todolist.DoToday.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleJoinMemberDto {
    private String id;
    private String email;
    private String name;
    private String picture;
}
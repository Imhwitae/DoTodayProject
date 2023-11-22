package com.todolist.DoToday.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

    ADMIN("ROLE_ADMIN"),
    BASIC_USER("ROLE_USER"),
    KAKAO_USER("KAKAO"),
    GOOGLE_USER("GOOGLE");

    private String role;
}

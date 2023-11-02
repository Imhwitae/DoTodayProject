package com.todolist.DoToday.entity;

import lombok.Getter;

public enum MemberRole {

    @Getter
    ADMIN("ROLE_ADMIN"),
    @Getter
    USER("ROLE_USER"),
    @Getter
    KAKAO_USER("KAKAO"),
    @Getter
    GOOGLE_USER("GOOGLE");


    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}

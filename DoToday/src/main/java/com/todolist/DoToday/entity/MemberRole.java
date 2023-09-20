package com.todolist.DoToday.entity;

import lombok.Getter;

public enum MemberRole {

    @Getter
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    MemberRole(String value) {
        this.value = value;
    }

    private String value;
}

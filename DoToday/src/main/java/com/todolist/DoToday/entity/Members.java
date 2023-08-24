package com.todolist.DoToday.entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Members {

    private Long membersId;
    private String email; //로그인 ID
    private String password;
    private String name; //사용자 이름
    private String picture;
    private Boolean secession; //탈퇴여부

    @Builder
    public Members(String email, String name, Boolean secession, String picture) {
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public Members update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }
}


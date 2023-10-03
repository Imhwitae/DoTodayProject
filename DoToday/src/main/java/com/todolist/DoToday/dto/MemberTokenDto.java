package com.todolist.DoToday.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class MemberTokenDto {
    private String accessToken;
    private String refreshToken;
}

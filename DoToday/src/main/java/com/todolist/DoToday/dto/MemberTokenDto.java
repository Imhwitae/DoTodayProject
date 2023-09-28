package com.todolist.DoToday.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberTokenDto {
    private String accessToken;
    private String refreshToken;
}

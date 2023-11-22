package com.todolist.DoToday.api.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokensDto {
    private String access_token;
    private String refresh_token;
}

package com.todolist.DoToday.api.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class TokensDto {
    private String status;
    private Map<String, Object> tokens;
//    private String access_token;
//    private String refresh_token;
}

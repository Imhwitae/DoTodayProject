package com.todolist.DoToday.dto.request;

import lombok.Getter;

@Getter
public class GoogleTokenDto {
    private String access_token;
    private Integer expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
}

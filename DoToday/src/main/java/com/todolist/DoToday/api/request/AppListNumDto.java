package com.todolist.DoToday.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AppListNumDto {
//    private String accessToken;
    @Schema(description = "받아온 리스트 번호", example = "1")
    private int listNum;
}

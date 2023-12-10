package com.todolist.DoToday.api.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AppListsOfMemberDto {//api로 보내줄 리스트를 담은 컬렉션과 유저 정보를 담은 토큰과 날짜를 보내줄 dto
    private List<AppListDto> list;
    @Schema(description = "조회한 날짜", example = "2023-12-09")
    private String date;
}

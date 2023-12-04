package com.todolist.DoToday.api.reponse;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AppListsOfMemberDto {//api로 보내줄 리스트를 담은 컬렉션과 유저 정보를 담은 토큰과 날짜를 보내줄 dto
    private List<AppListDto> list;
    private String accessToken;
    private String date;
}

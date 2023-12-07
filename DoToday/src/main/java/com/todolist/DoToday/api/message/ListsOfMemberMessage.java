package com.todolist.DoToday.api.message;

import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.reponse.AppListsOfMemberDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Setter
@Getter
public class ListsOfMemberMessage {//api로 보내줄 리스트를 담은 컬렉션과 유저 정보를 담은 토큰과 날짜를 보내줄 dto
    private AppListsOfMemberDto list;
    private String message;
    private HttpStatus status;
}

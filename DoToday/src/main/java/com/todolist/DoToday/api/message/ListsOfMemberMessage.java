package com.todolist.DoToday.api.message;

import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.reponse.AppListsOfMemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Setter
@Getter
public class ListsOfMemberMessage {//api로 보내줄 리스트를 담은 컬렉션과 유저 정보를 담은 토큰과 날짜를 보내줄 dto
    private AppListsOfMemberDto list;
    @Schema(description = "리스트 조회 성공 여부", example = "ListSearch_Success")
    private String message;
    @Schema(description = "HTTP 상태", example = "OK")
    private HttpStatus status;
}

package com.todolist.DoToday.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppListGetDto { // 받아서 작성, 수정용 dto
    @Schema(description = "리스트 번호", example = "1")
    private int listNum;
    @Schema(description = "리스트 내용", example = "test1")
    private String listTitle;
//    private String accessToken;
    @Schema(description = "작성날짜", example = "2023-12-09")
    private String date;
    @Schema(description = "언제할지", example = "아무때나")
    private String whenToDo;
    @Schema(description = "완료 여부(완료=1, 미완료=0)", example = "0")
    private int complete;
    @Schema(description = "작성 타입(작성=write,수정=update)", example = "write")
    private String type;//작성인지 수정인지 구분
}

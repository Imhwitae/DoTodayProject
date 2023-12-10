package com.todolist.DoToday.api.reponse;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppListDto { //리스트 내용을 담아서 보낼 dto
    @Schema(description = "리스트 번호", example = "1")
    private int listNum;
    @Schema(description = "리스트 내용", example = "test1")
    private String listTitle;
    @Schema(description = "리스트 완료 여부", example = "0")
    private int complete;
    @Schema(description = "언제 할지", example = "아무때나")
    private String whenToDo;
}

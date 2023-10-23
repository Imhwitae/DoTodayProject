package com.todolist.DoToday.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberChangePwDto {
    private String memberPrevPw;
    private String memberNewPw;
    private String memberConfNewPw;
}

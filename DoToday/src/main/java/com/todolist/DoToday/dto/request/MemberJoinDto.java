package com.todolist.DoToday.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {

    private String memberName;
    private String memberId;
    private String memberPw;
}

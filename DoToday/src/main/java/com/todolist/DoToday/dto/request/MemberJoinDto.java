package com.todolist.DoToday.dto.request;

import com.todolist.DoToday.dto.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MemberJoinDto {

    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberBirthYear;
    private String memberBirthMonth;
    private String memberBirthDate;
    private Gender memberGender;
    private String memberEmail;
}

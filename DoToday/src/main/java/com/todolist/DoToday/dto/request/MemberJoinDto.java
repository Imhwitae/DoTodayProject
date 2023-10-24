package com.todolist.DoToday.dto.request;

import com.todolist.DoToday.dto.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {
    private String memberId;
    private String memberEmailDomain;
    private String memberPw;
    private String memberName;
    private String memberBirthYear;
    private String memberBirthMonth;
    private String memberBirthDate;
    private Gender memberGender;
}

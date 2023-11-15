package com.todolist.DoToday.dto.request;

import com.todolist.DoToday.dto.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinDto {
    @NotBlank(message = "아이디를 입력하세요")
    private String memberId;
    private String memberEmailDomain;
    private String memberDirectEmail;
    @NotBlank(message = "비밀번호를 입력하세요")
    private String memberPw;
    private String memberName;
    private String memberBirthYear;
    private String memberBirthMonth;
    private String memberBirthDate;
    private Gender memberGender;
}

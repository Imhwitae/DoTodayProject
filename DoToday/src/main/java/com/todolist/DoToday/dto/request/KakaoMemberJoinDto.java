package com.todolist.DoToday.dto.request;

import com.todolist.DoToday.dto.Gender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class KakaoMemberJoinDto {
    private String memberId;
    private String memberName;
    private String memberBirthDay;
    private String memberImage;
    private Gender memberGender;
}

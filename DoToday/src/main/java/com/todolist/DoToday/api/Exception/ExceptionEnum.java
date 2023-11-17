package com.todolist.DoToday.api.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    //    회원 관련 오류
    MEMBER_OVERLAP_ERROR(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다.");

    private HttpStatus httpStatus;
    private String code;

}

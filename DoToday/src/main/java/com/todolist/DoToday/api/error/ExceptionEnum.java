package com.todolist.DoToday.api.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    //    회원 관련 오류
    MEMBER_OVERLAP_ERROR("ME01", HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    MEMBER_NOT_FOUND("ME02", HttpStatus.NOT_FOUND, "alredy used id");

    private final String code;
    private final HttpStatus httpStatus;
    private String msg;

}

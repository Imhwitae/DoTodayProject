package com.todolist.DoToday.api.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionEnum {
    // 회원 관련 오류
    MEMBER_OVERLAP_ERROR("ME01", HttpStatus.BAD_REQUEST, "Already used id"),
    MEMBER_NOT_FOUND("ME02", HttpStatus.NOT_FOUND, "Id not found"),
    MEMBER_PW_WRONG("ME03", HttpStatus.BAD_REQUEST, "This password is incorrect"),

    // request 관련 오류
    PARAMETER_ERROR("PE01", HttpStatus.BAD_REQUEST, "parmeterError"),

    // Token 관련 오류
    TOKEN_VALIDATE_ERROR("TE01", HttpStatus.BAD_REQUEST, "tokenValidateError"),
    TOKEN_TIMEOUT_ERROR("TE02", HttpStatus.BAD_REQUEST, "tokenTimeout");

    private final String code;
    private final HttpStatus httpStatus;
    private String msg;

}

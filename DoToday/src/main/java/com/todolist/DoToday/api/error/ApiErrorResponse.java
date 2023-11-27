package com.todolist.DoToday.api.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private String errorMsg;
    private HttpStatus status;
}

package com.todolist.DoToday.api.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ExceptionDto {
    private String errorCode;
    private String errorInfo;
}

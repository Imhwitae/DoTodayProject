package com.todolist.DoToday.api.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class Message {
    private String message;
    private HttpStatus status;
}

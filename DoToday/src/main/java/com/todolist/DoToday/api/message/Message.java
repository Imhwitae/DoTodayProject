package com.todolist.DoToday.api.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class Message {
    @Schema(description = "간략 설명", example = "OOOOO_Success")
    private String message;
    @Schema(description = "HTTP 상태", example = "OK")
    private HttpStatus status;
}

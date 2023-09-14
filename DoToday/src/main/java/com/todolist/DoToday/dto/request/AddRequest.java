package com.todolist.DoToday.dto.request;

import lombok.Data;

@Data
public class AddRequest {
    private String senderId;
    private String receiverId;
}

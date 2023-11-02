package com.todolist.DoToday.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Properties {
    private String nickname;
    private String profile_image;
    private String thumbnail_image;
}

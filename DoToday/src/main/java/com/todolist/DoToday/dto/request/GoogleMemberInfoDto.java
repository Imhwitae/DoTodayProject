package com.todolist.DoToday.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleMemberInfoDto {
    private String id;
    private String email;
    private boolean verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;
}

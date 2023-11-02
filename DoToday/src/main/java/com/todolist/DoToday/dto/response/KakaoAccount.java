package com.todolist.DoToday.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class KakaoAccount {
    private Boolean profile_nickname_needs_agreement;
    private Boolean profile_image_needs_agreement;
    private Profile profile;
    private Boolean has_email;
    private Boolean email_needs_agreement;
    private Boolean is_email_valid;
    private Boolean is_email_verified;
    private String email;
    private Boolean birthday_needs_agreement;
    private Boolean has_birthday;
    private String birthday;
    private String birthday_type;
    private Boolean has_gender;
    private Boolean gender_needs_agreement;
    private String gender;
}

package com.todolist.DoToday.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "소셜 회원가입 DTO")
public class ApiMemberJoinDto {
    @Schema(description = "회원가입 종류", example = "kakao")
    private String registration_id;  // kakao, naver, google...
    @Schema(description = "회원 아이디", example = "testuser1")
    private String id;  // email = id
    @Schema(description = "회원 비밀번호(해당 소셜의 UID사용)", example = "0000001")
    private String pw;  // uid = pw
    @Schema(description = "회원 이름", example = "testuser1")
    private String name;
    @Schema(description = "회원 이미지", example = "testimage")
    private String image_url;
//    private String gender;
}

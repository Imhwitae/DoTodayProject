package com.todolist.DoToday.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
public class OAuth2Attributes {
    private Map<String, Object> attributes;  // OAuth2ServiceImpl에서 받아온 attribute
    private String registrationId;
    private String name;
    private String email;
    private String gender;
    private String imageUrl;

    // 소셜 종류를 확인하는 메서드
//    public OAuth2Attributes divideSocial(String socialType, Map<String, Object> attributes) {
//        if (socialType.equals("google")) {
//        }
//    }


}

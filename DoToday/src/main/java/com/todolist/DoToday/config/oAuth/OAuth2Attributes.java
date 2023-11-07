package com.todolist.DoToday.config.oAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class OAuth2Attributes {
    private Map<String, Object> attributes;  // OAuth2ServiceImpl에서 받아온 attribute
    private String registrationId;
    private String userId;
    private String name;
    private String email;
    private char gender;
    private String imageUrl;

//     소셜 종류를 확인하는 메서드
    public static OAuth2Attributes divideSocial(String socialType, Map<String, Object> attributes) {
        log.info("divideSocial() 진입");
        if (socialType.equals("google")) {
            return typeGoogle(socialType, attributes);
        } else if (socialType.equals("kakao")) {
            return typeKakao(socialType, attributes);
        }
        return null;
    }

    private static OAuth2Attributes typeKakao(String registrationId, Map<String, Object> attributes) {
        String kakaoGender = String.valueOf(attributes.get("gender"));
        char gender =  kakaoGender.charAt(0);

        return OAuth2Attributes.builder()
                .registrationId(registrationId)
                .userId(String.valueOf(attributes.get("id")))
                .name(String.valueOf(attributes.get("nick_name")))  // String.valueOf()로 값이 없으면 null이 담김
                .email(String.valueOf(attributes.get("email")))
                .gender(gender)
                .imageUrl(String.valueOf(attributes.get("profile_image_url")))
                .attributes(attributes)
                .build();
    }

    private static OAuth2Attributes typeGoogle(String registrationId, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .registrationId(registrationId)
                .userId(String.valueOf(attributes.get("id")))
                .email(String.valueOf(attributes.get("email")))
                .name(String.valueOf(attributes.get("name")))
                .imageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .build();
    }
}

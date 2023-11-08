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
    private String gender;
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
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        String kakaoGender = String.valueOf(kakaoProfile.get("gender"));
        String gender =  kakaoGender.substring(0, 1);

        return OAuth2Attributes.builder()
                .registrationId(registrationId)
                .userId(String.valueOf(attributes.get("id")))
                .name(String.valueOf(kakaoProfile.get("nickname")))  // String.valueOf()로 값이 없으면 null이 담김
                .email(String.valueOf(kakaoAccount.get("email")))
                .gender(gender)
                .imageUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
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

package com.todolist.DoToday.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todolist.config.auth.OAuthToken;
import com.todo.todolist.domain.user.Role;
import com.todo.todolist.dto.response.KakaoProfile;
import com.todo.todolist.entity.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.naming.CommunicationException;

@RequiredArgsConstructor
@Component
@Slf4j
public class KakaoUserInfo {

    private OAuthToken oAuthToken;

    public Members getUserInfo(OAuthToken oAuthToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        // POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
        ResponseEntity<String> response2 = rt.exchange(
                "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest2,// 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Members members = Members.builder()
                .name(kakaoProfile.getProperties().getNickname())
                .email(kakaoProfile.getKakao_account().getEmail())
                .picture(kakaoProfile.getProperties().getProfile_image())
                .role(Role.USER)
                .secession(true)
                .build();

        return members;
    };

    public void kakaoUnlink(OAuthToken oAuthToken) throws CommunicationException {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>>request = new HttpEntity<>(null,headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/unlink",
                HttpMethod.POST,
                request,
                String.class
        );

        if(response.getStatusCode() == HttpStatus.OK){
            log.info("unlink"+response.getBody());
            return;
        }
        throw new CommunicationException();
    }
}

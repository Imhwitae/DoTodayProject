package com.todolist.DoToday.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.DoToday.config.oAuth.OAuthToken;
import com.todolist.DoToday.dto.Gender;
import com.todolist.DoToday.dto.request.KakaoMemberJoinDto;
import com.todolist.DoToday.dto.response.KakaoProfile;
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

    public KakaoMemberJoinDto getUserInfo(OAuthToken oAuthToken) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // POST 방식으로 Http 요청한다. 그리고 response 변수의 응답 받는다.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me", // https://{요청할 서버 주소}
                HttpMethod.POST, // 요청할 방식
                kakaoProfileRequest,// 요청할 때 보낼 데이터
                String.class // 요청 시 반환되는 데이터 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        KakaoMemberJoinDto members = new KakaoMemberJoinDto();
        members.setMemberId(kakaoProfile.getKakao_account().getEmail());
        members.setMemberName(kakaoProfile.getProperties().getNickname());
        members.setMemberImage(kakaoProfile.getProperties().getProfile_image());
        if (kakaoProfile.getKakao_account().getGender() == "male") {
            members.setMemberGender(Gender.Male);
        }else{
            members.setMemberGender(Gender.Female);
        }
//        log.info(kakaoProfile.getKakao_account()+"");
//        StringBuffer birth = new StringBuffer(kakaoProfile.getKakao_account().getBirthday());
//        members.setMemberBirthDay("0000-"+birth.insert(2,"-"));
        members.setMemberBirthDay("1111-11-11");

        return members;
    };

    public void kakaoLogout(OAuthToken oAuthToken) throws CommunicationException {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>>request = new HttpEntity<>(null,headers);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/user/logout",
                HttpMethod.POST,
                request,
                String.class
        );

        if(response.getStatusCode() == HttpStatus.OK){
            log.info("logout"+response.getBody());
            return;
        }
        throw new CommunicationException();
    }
}

package com.todolist.DoToday.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.DoToday.config.GoogleOauth;
import com.todolist.DoToday.dto.request.GoogleMemberInfoDto;
import com.todolist.DoToday.dto.request.GoogleTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleLoginService {
    private final GoogleOauth googleOauth;

    // 구글에서 준 code를 받아 GOOGLE_REQUEST_TOKEN_URL로 리다이렉트 후 토큰 얻기
    public ResponseEntity<String> responseTokenUrl(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("client_id", googleOauth.getClientId());
        params.put("client_secret", googleOauth.getClientSecret());
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", googleOauth.getGoogleRedirectUrl());

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(googleOauth.getGOOGLE_REQUEST_TOKEN_URL(),
                params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        } else {
           return null;
        }
    }

    // 구글에서 받은 토큰을 DTO에 저장하고 리턴
    public GoogleTokenDto googleTokenDto(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseEntity.getBody(), GoogleTokenDto.class);
    }

    // DTO에 저장된 토큰을 가져와서 GOOGLE_GET_USER_INFO_URL로 리다이렉트 후 유저 정보 리턴
    public ResponseEntity<String> getMemberInfo(GoogleTokenDto googleTokenDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleTokenDto.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(googleOauth.getGOOGLE_GET_USER_INFO_URL(),
                HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        return response;
    }

    // 구글에서 받은 유저 정보를 DTO에 저장하고 리턴
    public GoogleMemberInfoDto googleMemberInfoDto(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GoogleMemberInfoDto dto = objectMapper.readValue(responseEntity.getBody(), GoogleMemberInfoDto.class);
//        log.info("\n{}\n{}\n{}", dto.getName(), dto.getEmail(), dto.getPicture());
        return dto;
    }
}

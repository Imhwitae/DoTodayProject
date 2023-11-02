package com.todolist.DoToday.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.DoToday.config.GoogleOauth;
import com.todolist.DoToday.dto.request.GoogleJoinMemberDto;
import com.todolist.DoToday.dto.request.GoogleMemberInfoDto;
import com.todolist.DoToday.dto.request.GoogleTokenDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.entity.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleMemberService implements UserDetailsService, AuthenticationProvider {

    private final GoogleOauth googleOauth;
    private final JdbcTemplate jdbcTemplate;
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private String requestGooglecode;
    private final String LOGIN_URL = "http://localhost:8080/members/todologin";

    // 구글에서 준 code를 받아 GOOGLE_REQUEST_TOKEN_URL로 리다이렉트 후 토큰 얻기
    public ResponseEntity<String> responseTokenUrl(String code) {
        requestGooglecode = code;

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

    // 구글에서 받은 토큰을 리턴
    public GoogleTokenDto googleTokenDto(ResponseEntity<String> responseEntity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(responseEntity.getBody(), GoogleTokenDto.class);
    }

    // DTO에 저장된 토큰을 가져와서 GOOGLE_GET_USER_INFO_URL로 리다이렉트 후 유저 정보 리턴
    public ResponseEntity<String> requestMemberInfo(GoogleTokenDto googleTokenDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleTokenDto.getAccess_token());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(googleOauth.getGOOGLE_GET_USER_INFO_URL(),
                HttpMethod.GET, request, String.class);
        System.out.println(response.getBody());
        return response;
    }

    // 구글에서 받은 유저 정보를 리턴
    public GoogleMemberInfoDto getMemberInfoDto(String code) throws JsonProcessingException {
        ResponseEntity<String> getToken = responseTokenUrl(code);
        GoogleTokenDto savedToken = googleTokenDto(getToken);
        ResponseEntity<String> requestMember = requestMemberInfo(savedToken);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(requestMember.getBody(), GoogleMemberInfoDto.class);
    }

    // 구글 유저 정보 DB에 insert
    public void joinGoogleMember(GoogleMemberInfoDto googleMemberInfoDto) {
        String memberName = googleMemberInfoDto.getName();
        String memberId = googleMemberInfoDto.getEmail();
        String memberPw = bCryptPasswordEncoder.encode(googleMemberInfoDto.getId());
        String memberImage = googleMemberInfoDto.getPicture();
        LocalDate memberRegdate = LocalDate.now();
        String memberRole = MemberRole.getGOOGLE_USER().name();

        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_regdate, " +
                "member_role) values (?, ?, ?, ?, ?, ?)", memberId, memberPw, memberName, memberImage, memberRegdate,
                memberRole);
    }

    public ResponseEntity<String> responseUserName(GoogleMemberInfoDto googleMemberInfoDto) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> form = new HashMap<>();
        form.put("username", googleMemberInfoDto.getEmail());
        ResponseEntity<String> response = restTemplate.postForEntity(LOGIN_URL, form, String.class);
        System.out.println(response);
        return response;
    }

    // SpringSecurity와 연계해서 로그인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberService.findById(username);
    }

//    public void loadUser(String memberName) {
//        memberService.loadUserByUsername(memberName);
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        GoogleMemberInfoDto member;
//
//        try {
//            member = getMemberInfoDto(requestGooglecode);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        String memberId = authentication.getName();
//        String encodedPw = authentication.getCredentials().toString();
//        String usedGoogleIdForPw = member.getId();
//
//        if (bCryptPasswordEncoder.matches(usedGoogleIdForPw, encodedPw)) {
//            return new UsernamePasswordAuthenticationToken()
//        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}

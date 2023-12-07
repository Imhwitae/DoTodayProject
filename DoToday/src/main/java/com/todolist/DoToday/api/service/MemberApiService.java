package com.todolist.DoToday.api.service;

import com.todolist.DoToday.api.error.ApiErrorResponse;
import com.todolist.DoToday.api.reponse.MemberNumDto;
import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.api.request.ApiMemberLoginDto;
import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberApiService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private Map<String, Object> apiMap;

    // 기본 이미지 경로
    @Value("${cloud.aws.s3.basic-image}")
    private String basicImage;

    // 앱에서 받아온 정보로 회원가입
    public ResponseEntity<Map<String, Object>> apiMemberJoin(ApiMemberJoinDto apiMemberJoinDto) {
        apiMap = new HashMap<>();

        String id = apiMemberJoinDto.getId();
        String pw = apiMemberJoinDto.getPw();
        String name = apiMemberJoinDto.getName();
        String image = basicImage;
        if (StringUtils.hasText(apiMemberJoinDto.getImage_url()) && !apiMemberJoinDto.getImage_url().equals("null")) {
            image = apiMemberJoinDto.getImage_url();
        }
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = true;

        if (memberMapper.findById(id) != null) {
            apiMap.put("id error", id + " is already in use.");
            return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
        } else {
            String encodedPw = bCryptPasswordEncoder.encode(pw);
            memberMapper.ApiInsertMember(id, encodedPw, name, image, regtime, isEnabled);
            MemberNumDto num = new MemberNumDto(memberMapper.findById(id).getMemberNum());
            apiMap.put("joinSucess", num);
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        }
    }

    /* 처음 구현할 때 MemberService가 JwtTokenProvider에도 의존성 주입이 되어있어
       여기서 JwtTokenProvider를 가져다 쓰려고하면 순환 참조 오류 때문에 문제가 됐었다.
       그래서 myBatis로 DB접근 방식을 변경하여 순환 참조 오류를 해결함
       + 그냥 문자열로 Return하면 파싱을 못함!
    */
    public ResponseEntity<Map<String, Object>> checkMemberId(String id) {
        apiMap = new HashMap<>();

        try {
            MemberDetailDto member = memberMapper.findById(id);
            if (member.getMemberId() != null) {
                apiMap.put("error", id + " is already in use.");
                return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            apiMap.put("ok", id + " can be used");
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Map<String, Object>> apiLogin(ApiMemberLoginDto apiMemberLoginDto) {
        // 에러 처리하기 + 아이디 중복체크
        apiMap = new HashMap<>();

        try {
            MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
            bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
        } catch (NullPointerException e) {
//            ApiErrorResponse idError = new ApiErrorResponse(
//
//            );
            apiMap.put("error", e.getMessage());
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        }

        MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
        boolean checkPw = bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
        log.info("pw: {}", checkPw);

        if (checkPw) {
            MemberTokenDto appMemberToken = jwtTokenProvider.createToken(member.getMemberId());
            apiMap.put("loginSuccess", appMemberToken);
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        } else {
            apiMap.put("error", new ResponseEntity<>(HttpStatus.BAD_REQUEST));
            return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
        }

    }

//    public MemberTokenDto apiLogin(ApiMemberLoginDto apiMemberLoginDto) {
//        // 에러 처리하기 + 아이디 중복체크
//        apiMap = new HashMap<>();
//
//        try {
//            MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
//            bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
//        } catch (NullPointerException e) {
//            apiMap.put("error", e.getMessage());
////            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//            return null;
//        }
//
//        MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
//        boolean checkPw = bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
//        log.info("pw: {}", checkPw);
//
//        if (checkPw) {
//            MemberTokenDto appMemberToken = jwtTokenProvider.createToken(member.getMemberId());
//            apiMap.put("loginSuccess", appMemberToken);
////            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//            return appMemberToken;
//        } else {
//            apiMap.put("error", new ResponseEntity<>(HttpStatus.BAD_REQUEST));
////            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//            return null;
//        }
//    }
}

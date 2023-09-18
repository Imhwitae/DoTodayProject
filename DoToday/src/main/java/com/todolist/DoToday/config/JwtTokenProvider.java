package com.todolist.DoToday.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private String secretKey = "dotosecret";

    // 토큰 유효시간: 30분
    private long tokenTime = 30 * 60 * 1000L;

    // secretKey를 Base64로 인코딩
    @PostConstruct
    protected void encodeSecretKey() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String createToken(String memberId) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 토큰 알고리즘


        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

        Claims claims = Jwts.claims().setSubject(memberId);  // JWT payload에 저장되는 정보(보통 유저 식별 값을 넣음)
        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + tokenTime);

        return Jwts.builder()
                .setHeader(headerMap)  // 헤더에 들어갈 정보
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(expireTime)  // 토큰 시간 설정
                .signWith(key)  // 사용할 암호화 알고리즘
                .compact();
    }

    // JWT 토큰에서 회원 정보 추출
//    public String getMemberInfo(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .
//
//    }

    // JWT 토큰에서 인증정보 조회
//    public Authentication getAuth(String token) {
//
//    }
}

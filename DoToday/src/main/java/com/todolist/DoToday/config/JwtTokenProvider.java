package com.todolist.DoToday.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${custom.jwt.secret-key}")
    private String secretKey;

    // secretKey Base64로 인코딩하고 리턴 (plain Text 자체를 secret key로 사용하는 것을 권장하지 않음)
    public SecretKey jwtSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // 토큰 유효시간: 30분
    private long tokenTime = 30 * 60 * 1000L;

    // JWT 토큰 생성
    public String createToken(String memberId) {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

//        Claims claims = Jwts.claims().setSubject(memberId);  // JWT payload에 저장되는 정보(보통 유저 식별 값을 넣음)

        Date date = new Date();  // 현재 시간
        Date expireTime = new Date();
        expireTime.setTime(date.getTime() + tokenTime);  // 만료 시간

        return Jwts.builder()
                .setHeader(headerMap)  // 헤더에 들어갈 정보
//                .setClaims(claims)  // 정보 저장
                .setSubject(memberId)
                .setIssuedAt(date)  // 토큰 발행 일자
                .setExpiration(expireTime)  // 토큰 만료 시간
                .signWith(jwtSecretKey())  // 커스텀 키, 사용할 암호화 알고리즘(알아서 해줌)
                .compact();
    }

//     JWT 토큰 유효성 검증
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey())
                    .build()
                    .parseClaimsJws(token);  // JWT를 파싱
        } catch (JwtException e) {
            log.info("토큰 오류");
            e.printStackTrace();
        }
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

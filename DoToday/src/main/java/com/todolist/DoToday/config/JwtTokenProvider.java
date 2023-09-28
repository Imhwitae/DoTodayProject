package com.todolist.DoToday.config;

import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    private final String BEARER = "Bearer";
    private final Map<String, Object> headerMap = new HashMap<>();

    @Value("${custom.jwt.secret-key}")
    private String secretKey;

    // secretKey Base64로 인코딩하고 리턴 (plain Text 자체를 secret key로 사용하는 것을 권장하지 않음)
    public SecretKey jwtSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // acsessToken 유효시간: 30분 (30 * 10분)
    private long tokenTime = 30 * 60 * 1000L;
    // refreshToken 유효시간: 3일
    private long refreshTokenTime = 4320 * 60 * 1000L;

    // JWT 토큰 생성
    public String createToken(String memberId) {
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

//        Claims claims = Jwts.claims().setSubject(memberId);  // JWT payload에 저장되는 정보(보통 유저 식별 값을 넣음)

        // 현재 시간
        Date date = new Date();
        // accessToken 만료 시간
        Date expireTime = new Date();
        expireTime.setTime(date.getTime() + tokenTime);
        // refreshToken 만료 시간
        Date refreshExpireTime = new Date();
        refreshExpireTime.setTime(date.getTime() + refreshTokenTime);

        // accessToken 발급
        String accessToken = Jwts.builder()
                .setHeader(headerMap)  // 헤더에 들어갈 정보
                .claim("role", MemberRole.USER)  // 사용자 권한
                .setSubject(memberId)
                .setIssuedAt(date)  // 토큰 발행 일자
                .setExpiration(expireTime)  // 토큰 만료 시간
                .signWith(jwtSecretKey())  // 커스텀 키, 사용할 암호화 알고리즘(알아서 해줌)
                .compact();

        // refreshToken 발급
        String refreshToken = Jwts.builder()
                .setHeader(headerMap)
                .claim("role", MemberRole.USER)
                .setSubject(memberId)
                .setIssuedAt(date)
                .setExpiration(refreshExpireTime)
                .signWith(jwtSecretKey())
                .compact();

        // Dto에 저장
        MemberTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return accessToken;
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 서명 확인
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expireDate = claims.getExpiration();  // 토큰 만료 시간
            Date now = new Date();  // 현재 시간

            //  토큰 만료 시간이 현재 시간보다 이전이라면
            if (expireDate.before(now)) {
                log.info("accessToken 시간 만료");
                return false;
            }
        } catch (JwtException e) {
            log.info("토큰 검증 실패");
        }
        return true;
    }

    // JWT refreshToken 유효성 검증
    public String validateRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtSecretKey())
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                return reCreateAccessToken(claims.getSubject());
            }
        } catch (Exception e) {
            log.info("재로그인 필요");
        }
        return null;
    }

    // JWT 토큰 http header에 response
    public void accessTokenSetHeader(String accessToken, HttpServletResponse response) {
//        String headerValue = BEARER + accessToken;
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
        log.info(accessToken);
    }

    // JWT 토큰에서 사용자 아이디 추출
    public String getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * 토큰 추출
     * @StringUtils.hasText() 값이 있을 경우에는 true반환, 공백이거나 null이면 false 반환
     */
    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);  // http header의 authorization 이름을 가진 값을 가져옴

//        if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
//            return token.substring(7);
        return token;
    }

    public String reCreateAccessToken(String memberId) {
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");
        Date date = new Date();
        Date expireTime = new Date();
        expireTime.setTime(date.getTime() + tokenTime);

        String accessToken = Jwts.builder()
                .setHeader(headerMap)  // 헤더에 들어갈 정보
                .claim("role", MemberRole.USER)  // 사용자 권한
                .setSubject(memberId)
                .setIssuedAt(date)  // 토큰 발행 일자
                .setExpiration(expireTime)  // 토큰 만료 시간
                .signWith(jwtSecretKey())  // 커스텀 키, 사용할 암호화 알고리즘(알아서 해줌)
                .compact();

        return accessToken;
    }
}

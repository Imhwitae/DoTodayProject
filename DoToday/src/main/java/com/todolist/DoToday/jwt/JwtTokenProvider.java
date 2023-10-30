package com.todolist.DoToday.jwt;

import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.entity.MemberRole;
import com.todolist.DoToday.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final Map<String, Object> headerMap = new HashMap<>();
    private final MemberService memberService;

    @Value("${custom.jwt.secret-key}")
    private String secretKey;

    // secretKey Base64로 인코딩하고 리턴 (plain Text 자체를 secret key로 사용하는 것을 권장하지 않음)
    public SecretKey jwtSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // 시간은 밀리초 단위
    // acsessToken 유효시간: 60분 (60 * 10분)
    private long tokenTime = 60 * 60 * 1000L;
    // refreshToken 유효시간: 1일
    private long refreshTokenTime = 60 * 60 * 24 * 1000L;

    // JWT 토큰 생성
    public MemberTokenDto createToken(String memberId) {
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
                .claim("role", MemberRole.USER.name())  // 사용자 권한
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
        return MemberTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
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

    // JWT 토큰에서 사용자 아이디 추출
    public String getMemberIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // JWT 토큰에서 멤버 객체 추출
    public MemberDetailDto getMember(String token) {
        String memberId = getMemberIdFromToken(token);
        return memberService.findById(memberId);
    }

    // 쿠키 배열에서 accessToken만 추출
    public String extractToken(Cookie[] cookies) {
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String tokenName = cookie.getName();
                String value = cookie.getValue();

                if (tokenName.equals("refreshToken")) {
                    refreshToken = value;
                } else if (tokenName.equals("accessToken")){
                    accessToken = value;
                }
            }
        }

        return accessToken;
    }

    // accessToken 재발급
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

    public Authentication getAuthentication(String memberId) {
        MemberDetailDto member = memberService.findById(memberId);
        return new UsernamePasswordAuthenticationToken(member, null, null);
    }

}

package com.todolist.DoToday.handler;

import com.todolist.DoToday.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String memberId = authentication.getName();

        String getAccessToken = jwtTokenProvider.createToken(memberId).getAccessToken();
        String getRefreshToken = jwtTokenProvider.createToken(memberId).getRefreshToken();

        // accessToken 쿠키 생성
        Cookie accessToken = new Cookie("accessToken", getAccessToken);
        accessToken.setMaxAge(60*60);
        accessToken.setPath("/");

        // refreshToken 쿠키 생성
        Cookie refreshToken = new Cookie("refreshToken", getRefreshToken);
        refreshToken.setMaxAge(60*60*24);
        refreshToken.setPath("/");

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        log.info("{} 로그인 성공", memberId);

        if (jwtTokenProvider.getMember(memberId) == null) {
            response.sendRedirect("");
        } else {
            response.sendRedirect("/home");
        }
    }
}

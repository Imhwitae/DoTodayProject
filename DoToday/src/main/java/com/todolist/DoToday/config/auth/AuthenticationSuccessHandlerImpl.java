package com.todolist.DoToday.config.auth;

import com.todolist.DoToday.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
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
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String memberId = authentication.getName();
        String getAccessToken = jwtTokenProvider.createToken(memberId).getAccessToken();
        String getRefreshToken = jwtTokenProvider.createToken(memberId).getRefreshToken();

        // accessToken 쿠키 생성
        Cookie accessToken = new Cookie("accessToken", getAccessToken);
        accessToken.setMaxAge(60*30);
        accessToken.setPath(request.getContextPath());

        // refreshToken 쿠키 생성
        Cookie refreshToken = new Cookie("refreshToken", getRefreshToken);
        refreshToken.setMaxAge(60*60*24*30);
        refreshToken.setPath(request.getContextPath());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        response.sendRedirect("/home");
        log.info("{} 로그인 성공", memberId);
    }
}

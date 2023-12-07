package com.todolist.DoToday.handler;

//import com.todolist.DoToday.config.oAuth.CustomOAuth2User;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

        // accessToken 쿠키 생성
        Cookie accessToken = new Cookie("accessToken", getAccessToken);
        accessToken.setMaxAge(60*30);
        accessToken.setPath("/");

        response.addCookie(accessToken);
        log.info("{} 로그인 성공", memberId);
        response.sendRedirect("/home");
    }
}

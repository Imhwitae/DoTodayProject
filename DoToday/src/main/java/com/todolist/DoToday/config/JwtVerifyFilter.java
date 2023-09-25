package com.todolist.DoToday.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT를 검증 후 검증에 성공하면 SecurityContext에 저장하는 필터. OncePerRequestFilter를 상속받고 있다.
 * OncePerRequestFilter는 각 HTTP 요청에 대해 한 번만 실행되는 것을 보장한다.
 * HTTP 요청마다 JWT를 검증하는 것은 비효율적이기 때문에 OncePerRequestFilter를 상속함으로써 JWT 검증을 보다 효율적으로 수행할 수 있다.
 */
@Component
public class JwtVerifyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}

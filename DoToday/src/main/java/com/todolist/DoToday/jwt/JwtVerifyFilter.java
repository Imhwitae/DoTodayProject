package com.todolist.DoToday.jwt;

import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT를 검증 후 검증에 성공하면 SecurityContext에 저장하는 필터. OncePerRequestFilter를 상속받고 있다.
 * OncePerRequestFilter는 각 HTTP 요청에 대해 한 번만 실행되는 것을 보장한다.
 * HTTP 요청마다 JWT를 검증하는 것은 비효율적이기 때문에 OncePerRequestFilter를 상속함으로써 JWT 검증을 보다 효율적으로 수행할 수 있다.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String tokenName = cookie.getName();
                String value = cookie.getValue();

                if (tokenName.equals("refreshToken")) {
                    refreshToken = value;
//                    log.info("refreshToken {}", value);
                } else if (tokenName.equals("accessToken")){
                    accessToken = value;
//                    log.info("accessToken {}", value);
                }
            }
        }


        if (accessToken != null) {

            String memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);

            if (memberId != null && jwtTokenProvider.validateToken(accessToken)) {
                log.info("토큰 검증 후 시큐리티 컨텍스트에 정보 담기");
                Authentication authentication = jwtTokenProvider.getAuthentication(memberId);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("{} 유저 정보 저장", authentication.getName());
                log.info("인증 여부 = {}", SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                filterChain.doFilter(request, response);
            } else {
                log.info("멤버를 못 찾았거나, 토큰 오류");
                log.info("memberId = {}", memberId);
                log.info("accessToken = {}", accessToken);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

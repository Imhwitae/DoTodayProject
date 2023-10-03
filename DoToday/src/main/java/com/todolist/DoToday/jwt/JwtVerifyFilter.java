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
    private final MemberService memberService;

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
                    log.info("refreshToken {}", value);
                } else if (tokenName.equals("accessToken")){
                    accessToken = value;
                    log.info("accessToken {}", value);
                }
            }
        }

//        String memberAccessToken = memberTokenDto.getAccessToken();
//        String memberRefreshToken = memberTokenDto.getRefreshToken();
//        log.info("회원의 accessToken {}", memberAccessToken);
//
//        String memberId = jwtTokenProvider.getMemberIdFromToken(memberAccessToken);
//
//        // SecurityContextHolder에 인증 객체가 있는지 확인
//        if (memberId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // db 접근 말고 객체로 저장하기
//            MemberDetailDto member = memberService.findById(memberId);
//
//            if (!jwtTokenProvider.validateToken(memberAccessToken)) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                        = new UsernamePasswordAuthenticationToken(member, null, null);
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//
//                log.info("인증 정보 저장 {}", usernamePasswordAuthenticationToken.getName());
//            } else {
//                log.info("저장 실패");
//            }
//        }
        filterChain.doFilter(request, response);
    }

}

package com.todolist.DoToday.config;

import com.todolist.DoToday.handler.AuthenticationSuccessHandlerImpl;
import com.todolist.DoToday.jwt.JwtVerifyFilter;
import com.todolist.DoToday.config.oAuth.OAuth2ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
    private final JwtVerifyFilter jwtVerifyFilter;
    private final OAuth2ServiceImpl oAuth2Service;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/members/join_form", "/members/loginform", "/members/add",
                                "/css/**", "/scripts/**", "/plugin/**", "/image/**", "/kakao/login",
                                "/members/inputetc", "/api/members/join", "/api/list/write",
                                "/api/members/checkid").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/loginform")  // 사용자 정의 로그인 url
                        .loginProcessingUrl("/members/todologin")  // 로그인 진행 url
//                        .defaultSuccessUrl("/home")  // 로그인 성공 후 이동 url
                        .successHandler(authenticationSuccessHandlerImpl)
                        .failureUrl("/members/loginform?error")  // 로그인 실패시 작동
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2Service)
                        )
                        .successHandler(authenticationSuccessHandlerImpl)
                )
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .deleteCookies("JSESSIONID", "accessToken", "refreshToken")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}

package com.todolist.DoToday.config;

import com.todolist.DoToday.config.auth.AuthenticationSuccessHandlerImpl;
import com.todolist.DoToday.jwt.JwtVerifyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
    private final JwtVerifyFilter jwtVerifyFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/members/join_form", "/members/loginform", "/css/**", "/scripts/**", "/plugin/**", "/image/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtVerifyFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/loginform")  // 사용자 정의 로그인 url
                        .loginProcessingUrl("/members/login")  // 로그인 진행 url
//                        .defaultSuccessUrl("/home")  // 로그인 성공 후 이동 url
                        .successHandler(authenticationSuccessHandlerImpl)
                        .failureUrl("/members/loginform?error")  // 로그인 실패시 작동
                );

        return http.build();
    }


    // 패스워드 인코더 빈 등록
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

}

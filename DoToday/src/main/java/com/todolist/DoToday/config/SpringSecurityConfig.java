package com.todolist.DoToday.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/").permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/").permitAll()  // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/todo_main")  // 로그인 성공 후 이동 페이지
                        .failureUrl("/?error=true")  // 로그인 실패시 작동
                );

        return http.build();
    }
}

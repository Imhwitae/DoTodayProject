package com.todolist.DoToday.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
//import com.todolist.DoToday.config.GoogleOauth;
import com.todolist.DoToday.dto.request.GoogleMemberInfoDto;
import com.todolist.DoToday.dto.request.GoogleTokenDto;
//import com.todolist.DoToday.service.GoogleMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {
//    private final GoogleOauth googleOauth;
//    private final GoogleMemberService googleMemberService;

//    @GetMapping("/login/oauth2/code/callback/google")
//    public void googleTest() {
//        System.out.println("구글 로그인 테스트");
//    }
}

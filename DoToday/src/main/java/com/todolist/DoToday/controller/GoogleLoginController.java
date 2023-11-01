package com.todolist.DoToday.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todolist.DoToday.config.GoogleOauth;
import com.todolist.DoToday.dto.request.GoogleTokenDto;
import com.todolist.DoToday.service.GoogleLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GoogleLoginController {
    private final GoogleOauth googleOauth;
    private final GoogleLoginService googleLoginService;

    @GetMapping("/login/oauth2/code/callback/google")
    public ResponseEntity<String> getGoogleCode(@RequestParam(name = "code") String code) throws JsonProcessingException {
        ResponseEntity<String> responseEntity = googleLoginService.responseTokenUrl(code);
        GoogleTokenDto googleTokenDto = googleLoginService.googleTokenDto(responseEntity);
        ResponseEntity<String> info = googleLoginService.getMemberInfo(googleTokenDto);
        googleLoginService.googleMemberInfoDto(info);
        return info;
    }
}

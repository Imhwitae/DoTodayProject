package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.request.RequestRefreshToken;
import com.todolist.DoToday.service.TokenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenApiController {
    private final TokenApiService tokenApiService;

    @GetMapping("/validaccesstoken")
    public ResponseEntity<Map<String, Object>> validateAccessToken( ) {

    }

    @GetMapping("/validrefreshtoken")
    public ResponseEntity<Map<String, Object>> validateRefreshToken(@RequestBody RequestRefreshToken refreshToken) {
        return tokenApiService.validateRefreshToken(refreshToken.getRefreshToken());
    }
}

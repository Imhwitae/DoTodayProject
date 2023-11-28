package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.request.RequestAccessToken;
import com.todolist.DoToday.api.request.RequestRefreshToken;
import com.todolist.DoToday.api.service.TokenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenApiController {
    private final TokenApiService tokenApiService;

    @GetMapping("/validaccesstoken")
    public ResponseEntity<Map<String, Object>> validateAccessToken(@RequestBody RequestAccessToken accessToken) {
        return tokenApiService.validateAccessToken(accessToken.getAccessToken());
    }

    @GetMapping("/validrefreshtoken")
    public ResponseEntity<Map<String, Object>> validateRefreshToken(@RequestBody RequestRefreshToken refreshToken) {
        return tokenApiService.validateRefreshToken(refreshToken.getRefreshToken());
    }

    @PutMapping("/redirecttokentolist")
    public ResponseEntity<?> redirectToListController(@RequestBody RequestAccessToken accessToken) throws URISyntaxException {
        return tokenApiService.redirectAccessToken(accessToken.getAccessToken());
    }

}

package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.request.RequestAccessToken;
import com.todolist.DoToday.api.request.RequestRefreshToken;
import com.todolist.DoToday.api.service.TokenApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Map;

@Tag(name = "토큰 API", description = "토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenApiController {
    private final TokenApiService tokenApiService;

    @Operation(summary = "access token 검증", description = "access token을 받아 검증")
    @GetMapping("/validaccesstoken")
    public ResponseEntity<Map<String, Object>> validateAccessToken(@RequestBody RequestAccessToken accessToken) {
        return tokenApiService.validateAccessToken(accessToken.getAccessToken());
    }

    @Operation(summary = "refresh token 검증", description = "refresh token을 받아 검증후 유효한 토큰이라면 access token 재발급")
    @GetMapping("/validrefreshtoken")
    public ResponseEntity<Map<String, Object>> validateRefreshToken(@RequestBody RequestRefreshToken refreshToken) {
        return tokenApiService.validateRefreshToken(refreshToken.getRefreshToken());
    }

    @Operation(summary = "access token 토큰 리다이렉트", description = "access token을 받아 토큰 정보를 기반으로 리스트를 가져올 수 있도록 리다이렉트")
    @PutMapping("/redirecttokentolist")
    public ResponseEntity<Map<String, Object>> redirectToListController(@RequestBody RequestAccessToken accessToken) throws URISyntaxException {
        return tokenApiService.redirectAccessToken(accessToken.getAccessToken());
    }
}

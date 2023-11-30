package com.todolist.DoToday.api.service;

import com.todolist.DoToday.api.error.ApiErrorResponse;
import com.todolist.DoToday.api.error.ExceptionEnum;
import com.todolist.DoToday.api.reponse.AcccessTokenApi;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenApiService {
    private final JwtTokenProvider jwtTokenProvider;
    private Map<String, Object> apiMap;

    // accesstoken 검증
    public ResponseEntity<Map<String, Object>> validateAccessToken(String accessToken) {
        apiMap = new HashMap<>();

        if (StringUtils.hasText(accessToken) && jwtTokenProvider.validateToken(accessToken)) {
            String memberId = jwtTokenProvider.getMember(accessToken).getMemberId();
            apiMap.put("success", memberId);
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        } else if (accessToken.isEmpty()) {
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    ExceptionEnum.PARAMETER_ERROR.getMsg(),
                    ExceptionEnum.PARAMETER_ERROR.getHttpStatus()
            );
            apiMap.put("error", errorResponse);
            return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
        } else {
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    ExceptionEnum.TOKEN_VALIDATE_ERROR.getMsg(),
                    ExceptionEnum.TOKEN_VALIDATE_ERROR.getHttpStatus()
            );
            apiMap.put("error", errorResponse);
            return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
        }
    }

    // refreshToken 검증 후 accessToken 재발급
    public ResponseEntity<Map<String, Object>> validateRefreshToken(String refreshToken) {
        apiMap = new HashMap<>();
        if (StringUtils.hasText(refreshToken) && !refreshToken.isEmpty()) {
            log.info("tokenOK");
            if (jwtTokenProvider.validateToken(refreshToken)) {
                String memberId = jwtTokenProvider.getMemberIdFromToken(refreshToken);
                AcccessTokenApi accessToken = new AcccessTokenApi(jwtTokenProvider.reCreateAccessToken(memberId));
                apiMap.put("validSuccess", accessToken);
                log.info("validOK");
                return new ResponseEntity<>(apiMap, HttpStatus.OK);
            } else {
                ApiErrorResponse errorResponse = new ApiErrorResponse(
                        ExceptionEnum.TOKEN_TIMEOUT_ERROR.getMsg(), ExceptionEnum.TOKEN_TIMEOUT_ERROR.getHttpStatus());
                apiMap.put("error", errorResponse);
                log.info("TokenValidErr");
                return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
            }
        } else {
            ApiErrorResponse errorResponse = new ApiErrorResponse(
                    ExceptionEnum.PARAMETER_ERROR.getMsg(), ExceptionEnum.PARAMETER_ERROR.getHttpStatus());
            apiMap.put("error", errorResponse);
            log.info("parmeterErr");
            return new ResponseEntity<>(apiMap ,HttpStatus.BAD_REQUEST);
        }
    }

    // accessToken redirect
    public ResponseEntity<?> redirectAccessToken(String accessToken) throws URISyntaxException {
        URI redirectUrl = new URI("http://localhost:8080/api/list/show");
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);
        headers.setLocation(redirectUrl);
        System.out.println(headers);
        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }
}

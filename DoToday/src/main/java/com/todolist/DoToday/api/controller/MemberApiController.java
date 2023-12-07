package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.request.ApiCheckMemberIdDto;
import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.api.request.ApiMemberLoginDto;
import com.todolist.DoToday.api.service.MemberApiService;
import com.todolist.DoToday.dto.MemberTokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "회원 API", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberApiService memberApiService;

    @Operation(summary = "회원 가입", description = "회원가입 양식을 DB에 삽입")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "success",
                    content = @Content(
                            schema = @Schema(implementation = ApiMemberJoinDto.class)
                    ))
    })
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> apiSocialMemberJoin(@RequestBody ApiMemberJoinDto joinApi) throws IOException {
        return memberApiService.apiMemberJoin(joinApi);
    }

    @Operation(summary = "회원 중복 체크", description = "회원 아이디를 받아 중복체크")
    @PostMapping("/checkid")
    public ResponseEntity<Map<String, Object>> apiCheckMember(@RequestBody ApiCheckMemberIdDto id) {
        return memberApiService.checkMemberId(id.getMemberId());
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 받고 로그인 성공시 토큰 반환")
    @PostMapping("/login")
    public ResponseEntity<Object> apiLoginMember(@RequestBody ApiMemberLoginDto apiMemberLoginDto) {
        return memberApiService.apiLogin(apiMemberLoginDto);
    }
}



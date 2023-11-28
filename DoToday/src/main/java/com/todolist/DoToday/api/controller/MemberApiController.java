package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.request.ApiCheckMemberIdDto;
import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.api.request.ApiMemberLoginDto;
import com.todolist.DoToday.api.request.RequestRefreshToken;
import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> apiSocialMemberJoin(@RequestBody ApiMemberJoinDto joinApi) throws IOException {
        return memberService.apiMemberJoin(joinApi);
    }

    @PostMapping("/checkid")
    public ResponseEntity<Map<String, Object>> apiCheckMember(@RequestBody ApiCheckMemberIdDto id) {
        return memberService.checkMemberId(id.getMemberId());
    }

    @PostMapping("/login")
    public MemberTokenDto apiLoginMember(@RequestBody ApiMemberLoginDto apiMemberLoginDto) {
        return memberService.apiLogin(apiMemberLoginDto);
    }
}



package com.todolist.DoToday.api;

import com.todolist.DoToday.api.request.ApiCheckMemberIdDto;
import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.api.request.ApiMemberLoginDto;
import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long apiSocialMemberJoin(@RequestBody ApiMemberJoinDto joinApi) throws IOException {
        return memberService.apiMemberJoin(joinApi);
    }

    @PostMapping("/checkid")
    public String apiCheckMember(@RequestBody ApiCheckMemberIdDto id) {
        return memberService.checkMemberId(id.getMemberId());
    }

    @PostMapping("/login")
    public ResponseEntity<Object> apiLoginMember(@RequestBody ApiMemberLoginDto apiMemberLoginDto) {
        return memberService.apiLogin(apiMemberLoginDto);
    }

}



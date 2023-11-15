package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.AppMemberJoinDto;
import com.todolist.DoToday.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class APIController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long appSocialMemberJoin(@RequestBody AppMemberJoinDto joinApi) {
        return memberService.appMemberJoin(joinApi);
    }

//    @GetMapping("/view")

}



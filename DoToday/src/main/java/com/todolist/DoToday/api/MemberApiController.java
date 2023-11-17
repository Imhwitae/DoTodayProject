package com.todolist.DoToday.api;

import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Long appSocialMemberJoin(@RequestBody ApiMemberJoinDto joinApi) {
        return memberService.appMemberJoin(joinApi);
    }

//    @GetMapping("/view")

}



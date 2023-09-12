package com.todolist.DoToday.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class GoogleLoginController {

    @GetMapping("/login/oauth2/code/callback/google")
    public void googleLogin(@RequestParam("code") String accessCode) {
//        memberService.getCodeTest(accessCode);
    }
}

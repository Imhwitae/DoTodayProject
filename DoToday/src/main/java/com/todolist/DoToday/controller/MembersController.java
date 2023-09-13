package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.auth.OAuthToken;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.service.MemberService;
import com.todolist.DoToday.util.KakaoToken;
import com.todolist.DoToday.util.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MembersController {
    private final MemberService memberService;
    private MemberJoinDto memberJoinDto;

    @GetMapping("/join_form")
    public String addForm(Model model) {

        model.addAttribute("member", new MemberJoinDto());

        return "/join_form";
    }

    @PostMapping("/add")
    public String memberAdd(MemberJoinDto memberJoinDto) {
        String name = memberJoinDto.getMemberName();
        System.out.println("가입 완료: " + name);
        return "redirect:/";
    }

}

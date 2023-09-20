package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.auth.OAuthToken;
import com.todolist.DoToday.dto.Gender;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.request.MemberLoginDto;
import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.service.MemberService;
import com.todolist.DoToday.util.KakaoToken;
import com.todolist.DoToday.util.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MembersController {
    private final MemberService memberService;

    @GetMapping("/join_form")
    public String addForm(Model model) {

        model.addAttribute("member", new MemberJoinDto());

        return "/join_form";
    }

    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();  // enum에서 values를 사용하면 모든 데이터를 배열로 만들어줌
    }

    @PostMapping("/add")
    public String memberAdd(MemberJoinDto memberJoinDto) {

        memberService.joinMember(memberJoinDto);

        log.info("회원가입 성공");

        return "/member/login";
    }

    @GetMapping("/loginform")
    public String memberLoginForm() {
//        model.addAttribute("loginForm", new MemberLoginDto());
        return "/member/login";
    }

//    @PostMapping("/login")
//    public String memberLogin(MemberLoginDto memberLoginDto) {
//        memberService.loadUserByUsername(memberLoginDto.getMemberId());
//        log.info("로그인");
//
//        return "redirect:/todo_main";
//    }

//    @GetMapping("/home")
//    public String goHome() {
//        return "/todo_main";
//    }



}

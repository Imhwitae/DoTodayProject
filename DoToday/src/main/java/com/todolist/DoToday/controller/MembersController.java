package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.Gender;
import com.todolist.DoToday.dto.request.MemberChangePwDto;
import com.todolist.DoToday.dto.request.MemberEtcInfoDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MembersController {

    private final MemberService memberService;

    @GetMapping("/join_form")
    public String addForm(Model model) {

        model.addAttribute("member", new MemberJoinDto());

        return "join_form";
    }

    // 모든 필드를 배열로 만들어줌
    @ModelAttribute("genders")
    public Gender[] genders() {
        return Gender.values();
    }

    @PostMapping("/add")
    public String memberAdd(@Valid MemberJoinDto memberJoinDto) {
        log.info("id: {}, " +
                "email: {}, " +
                "pw: {}, " +
                "name: {}",
                memberJoinDto.getMemberId(), memberJoinDto.getMemberEmailDomain(), memberJoinDto.getMemberPw(),
                memberJoinDto.getMemberName());

        memberService.joinMember(memberJoinDto);

        log.info("회원가입 성공");

        return "member/login";
    }

    @GetMapping("/loginform")
    public String memberLoginForm() {
        return "member/login";
    }

    // @AuthenticationPricipal로 바꾸기
    @GetMapping("/mypage")
    public String memberMyPage(@AuthenticationPrincipal MemberDetailDto member, Model model) {
//        String accessToken = jwtTokenProvider.extractToken(request.getCookies());
        model.addAttribute("memberDetail", member);
        model.addAttribute("changePw", new MemberChangePwDto());
        return "member/mypage";
    }

    @PostMapping("/update")
    public String updateMyPage(@AuthenticationPrincipal MemberDetailDto member,
                               @RequestParam("image_file") MultipartFile image) throws IOException {
        log.info("이미지 받음 {}", image.getOriginalFilename());

//        String memberId = jwtTokenProvider.getMemberIdFromToken(jwtTokenProvider.extractToken(request.getCookies()));
        String memberId = member.getMemberId();
        memberService.updateMemberImg(image, memberId);

        return "redirect:/list/todolist";
    }

    @PostMapping("/pwupdate")
    public String updatePw(@AuthenticationPrincipal MemberDetailDto memberDetailDto, MemberChangePwDto memberChangePwDto) {
        memberService.updateMemberPw(memberDetailDto, memberChangePwDto);
        return "redirect:/members/logout";
    }

    @GetMapping("/writeetc")
    public String writeEtc(Model model) {
        model.addAttribute("memberEtcInfo", new MemberJoinDto());
        return "member/memberInputEtcInfo";
    }

    @PostMapping("/inputetc")
    public String inputEtc(MemberJoinDto memberJoinDto) {
        memberService.inputEtc(memberJoinDto);
        return "redirect:/members/mypage";
    }

}

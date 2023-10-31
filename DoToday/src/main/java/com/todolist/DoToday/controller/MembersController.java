package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.Gender;
import com.todolist.DoToday.dto.request.MemberChangePwDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtTokenProvider jwtTokenProvider;

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
        log.info("id: {}, " +
                "email: {}, " +
                "pw: {}, " +
                "name: {}",
                memberJoinDto.getMemberId(), memberJoinDto.getMemberEmailDomain(), memberJoinDto.getMemberPw(),
                memberJoinDto.getMemberName());

        memberService.joinMember(memberJoinDto);

        log.info("회원가입 성공");

        return "/member/login";
    }

    @GetMapping("/loginform")
    public String memberLoginForm() {
        return "/member/login";
    }

    // @AuthenticationPricipal로 바꾸기
    @GetMapping("/mypage")
    public String memberMyPage(HttpServletRequest request, Model model) {
        String accessToken = jwtTokenProvider.extractToken(request.getCookies());
        model.addAttribute("memberDetail", jwtTokenProvider.getMember(accessToken));
        model.addAttribute("changePw", new MemberChangePwDto());
        return "/member/mypage";
    }

//    @GetMapping("/changeMemInfo")
//    public String changeMemberInfo(@AuthenticationPrincipal MemberDetailDto memberDetailDto,
//                                   Model birthModel,
//                                   Model changePwModel) {
//        changePwModel.addAttribute("changePw", new MemberChangePwDto());
//        return "/member/change_mem_info";
//    }

    @PostMapping("/update")
    public String updateMyPage(HttpServletRequest request,
                               @RequestParam("image_file") MultipartFile image) throws IOException {
        log.info("이미지 받음 {}", image.getOriginalFilename());

        String memberId = jwtTokenProvider.getMemberIdFromToken(jwtTokenProvider.extractToken(request.getCookies()));
        memberService.updateMemberImg(image, memberId);

        return "redirect:/list/todolist";
    }

    @PostMapping("/pwupdate")
    public String updatePw(@AuthenticationPrincipal MemberDetailDto memberDetailDto, MemberChangePwDto memberChangePwDto) {
        memberService.updateMemberPw(memberDetailDto, memberChangePwDto);
        return "redirect:/members/logout";
    }

}

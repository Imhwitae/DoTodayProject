package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.auth.OAuthToken;
import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.util.KakaoToken;
import com.todolist.DoToday.util.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/members")
@RequiredArgsConstructor
public class MembersController {
//    private final MemberService memberService;
    private final KakaoToken kakaoToken;
    private final KakaoUserInfo kakaoUserInfo;

    @GetMapping("/kakao/login")
    public String kakaoCallback(String code, Model model) {

        OAuthToken oAuthToken = kakaoToken.getToken(code);

        System.out.println("카카오 엑세스 토큰 : "+ oAuthToken.getAccess_token());

        Members kakaoProfile = kakaoUserInfo.getUserInfo(oAuthToken);

        System.out.println("kakao이메일 = " + kakaoProfile.getEmail());

        try {
            memberService.join(kakaoProfile);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/message";
        }

        return "member/message3";
    }

    @GetMapping("/kakao/index")
    public String index(){
        return "index";
    }



}

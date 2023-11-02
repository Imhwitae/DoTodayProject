package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.oAuth.OAuthToken;
import com.todolist.DoToday.dto.request.KakaoMemberJoinDto;
import com.todolist.DoToday.service.MemberService;
import com.todolist.DoToday.util.KakaoToken;
import com.todolist.DoToday.util.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.naming.CommunicationException;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {

    private final KakaoToken kakaoToken;
    private final KakaoUserInfo kakaoUserInfo;
    private final MemberService memberService;

    @GetMapping("/loginForm")
    public String loginForm(){
        return "index";
    }

    @GetMapping("/kakao/login")
    public String kakaoCallback(String code) throws Exception {

        OAuthToken oAuthToken = kakaoToken.getToken(code);

        System.out.println("카카오 엑세스 토큰 : "+ oAuthToken.getAccess_token());

        KakaoMemberJoinDto kakaoProfile = kakaoUserInfo.getUserInfo(oAuthToken);

        System.out.println("kakao이메일 = " + kakaoProfile.getMemberId());

        if (memberService.findById(kakaoProfile.getMemberId()) == null){//카카오 유저 정보가 없을때
            memberService.kakaoJoinMember(kakaoProfile);
        }else{
            kakaoUserInfo.kakaoLogout(oAuthToken);
        }

        return "redirect:/";
    }

    @GetMapping("/kakao/logout")
    public String kakaoUnlink(String code) throws CommunicationException {
        OAuthToken oAuthToken = kakaoToken.getToken(code);

        System.out.println("카카오 엑세스 토큰 : "+ oAuthToken.getAccess_token());

        kakaoUserInfo.kakaoLogout(oAuthToken);
        return "redirect:/";
    }
}

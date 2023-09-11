package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.auth.OAuthToken;
import com.todolist.DoToday.entity.Members;
import com.todolist.DoToday.util.KakaoToken;
import com.todolist.DoToday.util.KakaoUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/members")
@RequiredArgsConstructor
public class MembersController {
//    private final FirebaseServiceKakaoImpl firebaseServiceKakao;
    private final KakaoToken kakaoToken;
    private final KakaoUserInfo kakaoUserInfo;
//    private final MemberService memberService;

    @GetMapping("/kakao/login")
    public String kakaoCallback(String code, Model model) throws Exception {

        OAuthToken oAuthToken = kakaoToken.getToken(code);

        System.out.println("카카오 엑세스 토큰 : "+ oAuthToken.getAccess_token());

        Members kakaoProfile = kakaoUserInfo.getUserInfo(oAuthToken);

        System.out.println("kakao이메일 = " + kakaoProfile.getEmail());

        try {
//            firebaseServiceKakao.insert(kakaoProfile);
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

    @GetMapping("join")
    public String joinPage() {
        return "test_form";
    }


    // 구글 로그인
    @GetMapping("/login/oauth2/code/callback/google")
    public void googleLogin(@RequestParam("code") String accessCode) {
//        memberService.getCodeTest(accessCode);
    }

}

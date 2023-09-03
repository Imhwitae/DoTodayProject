package com.todolist.DoToday.controller;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(@RequestParam("code") String code) {
        // 카카오로부터 받은 코드를 사용하여 사용자 정보 요청 및 인증 처리를 수행하세요.
        // 카카오 API를 사용하여 사용자 정보를 가져오고 Firebase Authentication에 등록합니다.
        // Firebase 사용자 토큰을 생성하고 반환합니다.

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            // Firebase 사용자 토큰을 생성하고 반환할 수 있습니다.
            String firebaseIdToken = generateFirebaseIdToken(decodedToken.getUid());
            return "redirect:/success";
        } catch (FirebaseAuthException e) {
            // 오류 처리
            return "redirect:/error";
        }
    }

    // Firebase 사용자 토큰 생성 로직
    private String generateFirebaseIdToken(String uid) {
        // Firebase 사용자 토큰 생성 코드를 추가하세요.
        return "YourFirebaseIdTokenHere";
    }
}


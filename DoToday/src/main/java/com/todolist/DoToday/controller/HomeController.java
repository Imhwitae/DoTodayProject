package com.todolist.DoToday.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request) {
        String accessToken = null;
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String tokenName = cookie.getName();
                String value = cookie.getValue();

                if (tokenName.equals("refreshToken")) {
                    refreshToken = value;
                } else if (tokenName.equals("accessToken")){
                    accessToken = value;
                }
            }
        }

        if (accessToken != null) {
            return "redirect:/list/todolist";
        }

        return "/index.html";
    }

    @GetMapping("/home")
    public String listHome() {
        return "redirect:/list/todolist";
    }
}

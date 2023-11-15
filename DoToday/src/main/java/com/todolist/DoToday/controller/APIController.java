package com.todolist.DoToday.controller;

import com.todolist.DoToday.config.oAuth.OAuth2ServiceImpl;
import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class APIController {

    private final OAuth2ServiceImpl oAuth2Service;

    @PostMapping("/join")
    public RequestEntity<SocialMemberJoinDto> appSocialMemberJoin(@RequestBody SocialMemberJoinDto socialMemberJoinDto) {

    }



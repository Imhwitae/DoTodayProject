package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.OAuth2Attributes;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


public class OAuth2ServiceImpl implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // oauth2 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 소셜 로그인 정보

        // 가져올 attributes는 각 소셜 서비스마다 다르기 때문에 각각에 맞게 대응해야 한다.
        Map<String, Object> originAttribute = oAuth2User.getAttributes();

        OAuth2Attributes attributes = OAuth2Attributes.divideSocial(registrationId, originAttribute);
        return null;
    }

//    private MemberJoinDto
}

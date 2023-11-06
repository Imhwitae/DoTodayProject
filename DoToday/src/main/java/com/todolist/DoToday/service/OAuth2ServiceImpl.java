package com.todolist.DoToday.service;

import com.todolist.DoToday.dao.MemberDao;
import com.todolist.DoToday.domain.CustomOAuth2User;
import com.todolist.DoToday.dto.OAuth2Attributes;
import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2ServiceImpl implements OAuth2UserService<OAuth2UserRequest,OAuth2User> {
    private final MemberDao memberDao;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // oauth2 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 소셜 로그인 정보

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();  // 권한 설정
        grantedAuthorities.add(new SimpleGrantedAuthority(registrationId));

        // 가져올 attributes는 각 소셜 서비스마다 다르기 때문에 각각에 맞게 대응해야 한다.
        Map<String, Object> originAttribute = oAuth2User.getAttributes();
        String email = originAttribute.get("email").toString();
        OAuth2Attributes attributes = OAuth2Attributes.divideSocial(registrationId, originAttribute);
        memberDao.joinSocialMember(socialMember(attributes));
        log.info("소셜 로그인 서비스");

        return new CustomOAuth2User(registrationId, originAttribute, email, grantedAuthorities);
    }

    private SocialMemberJoinDto socialMember(OAuth2Attributes attributes) {
        return SocialMemberJoinDto.builder()
                .memberId(attributes.getEmail())
                .memberPw(attributes.getUserId())
                .memberName(attributes.getName())
                .memberImage(attributes.getImageUrl())
                .build();
    }
}

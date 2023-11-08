package com.todolist.DoToday.config.oAuth;

import com.todolist.DoToday.dao.MemberDao;
//import com.todolist.DoToday.config.oAuth.CustomOAuth2User;
import com.todolist.DoToday.config.oAuth.OAuth2Attributes;
import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2ServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberDao memberDao;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser() 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // oauth2 정보 가져오기

        /**
         * userRequest에서 registrationId 추출
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 소셜 로그인 정보
        log.info(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();  // OAuth 로그인시 키가 되는 값
        log.info(userNameAttributeName);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();  // 권한 설정
        grantedAuthorities.add(new SimpleGrantedAuthority(registrationId));

        // 가져올 attributes는 각 소셜 서비스마다 다르기 때문에 각각에 맞게 대응해야 한다.
        Map<String, Object> originAttribute = oAuth2User.getAttributes();
        OAuth2Attributes attributes = OAuth2Attributes.divideSocial(registrationId, originAttribute);

        SocialMemberJoinDto socialMember = socialMember(attributes);
        memberDao.joinSocialMember(socialMember);

//        return new CustomOAuth2User(grantedAuthorities, originAttribute, userNameAttributeName,
//                socialMember.getMemberId(), registrationId);
        return new MemberDetailDto(socialMember);
    }

    private SocialMemberJoinDto socialMember(OAuth2Attributes attributes) {
        return SocialMemberJoinDto.builder()
                .memberId(attributes.getEmail())
                .memberPw(attributes.getUserId())
                .memberName(attributes.getName())
                .memberImage(attributes.getImageUrl())
                .regdate(LocalDate.now())
                .memberExpired(false)
                .build();
    }
}

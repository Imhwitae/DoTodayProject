package com.todolist.DoToday.dto.response;

import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class MemberDetailDto implements UserDetails, OAuth2User {
    private Long memberNum;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberImage;
    private boolean memberEnabled;
    private Map<String, Object> attributes;

    public MemberDetailDto(Long memberNum, String memberId, String memberPw, String memberName, String memberImage, boolean memberEnabled) {
        this.memberNum = memberNum;
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberName = memberName;
        this.memberImage = memberImage;
        this.memberEnabled = memberEnabled;
    }

    public MemberDetailDto (SocialMemberJoinDto socialMemberJoinDto) {
        this.memberId = socialMemberJoinDto.getMemberId();
        this.memberPw = socialMemberJoinDto.getMemberPw();
        this.memberName = socialMemberJoinDto.getMemberName();
        this.memberImage = socialMemberJoinDto.getMemberImage();
        this.memberEnabled = socialMemberJoinDto.isMemberEnabled();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return memberPw;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 소셜 로그인 아이디 리턴
    @Override
    public String getName() {
        return memberId;
    }
}

package com.todolist.DoToday.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class MemberDetailDto implements UserDetails, OAuth2User {

    private Long memberNum;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberImage;
    private boolean memberExpired;
    private Map<String, Object> attributes;

    // 기본 로그인시 가져오는 정보
    public MemberDetailDto(Long memberNum, String memberId, String memberPw, String memberName, String memberImage, boolean memberExpired) {
        this.memberNum = memberNum;
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberName = memberName;
        this.memberImage = memberImage;
        this.memberExpired = memberExpired;
    }



    @Override
    public Map<String, Object> getAttributes() {
        return null;
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

    @Override
    public String getName() {
        return null;
    }
}

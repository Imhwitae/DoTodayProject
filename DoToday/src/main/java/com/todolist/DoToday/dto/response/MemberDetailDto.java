package com.todolist.DoToday.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class MemberDetailDto implements UserDetails {

    private Long memberNum;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberImage;
    private boolean memberExpired;

    public MemberDetailDto(Long memberNum, String memberId, String memberPw, String memberName, String memberEmail, String memberImage, boolean memberExpired) {
        this.memberNum = memberNum;
        this.memberId = memberId;
        this.memberPw = memberPw;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberImage = memberImage;
        this.memberExpired = memberExpired;
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
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

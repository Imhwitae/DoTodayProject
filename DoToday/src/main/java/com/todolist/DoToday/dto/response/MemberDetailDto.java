package com.todolist.DoToday.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Builder
public class MemberDetailDto implements UserDetails {

    private String memberId;
    private String memberName;
    private String memberEmail;
    private String memberImage;

    public MemberDetailDto(String memberId, String memberName, String memberEmail, String memberImage) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.memberImage = memberImage;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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

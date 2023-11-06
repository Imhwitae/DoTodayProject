package com.todolist.DoToday.dto.request;

import com.todolist.DoToday.dto.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
@Builder
public class SocialMemberJoinDto {
    private String memberId;
    private String memnerPw;
    private String memberName;
    private String memberImage;
    private Collection<GrantedAuthority> role; // DB에 적용 되는지 안되는지 확인
}

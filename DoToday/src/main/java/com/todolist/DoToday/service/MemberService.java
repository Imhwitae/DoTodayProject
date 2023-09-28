package com.todolist.DoToday.service;

import com.todolist.DoToday.config.JwtTokenProvider;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService, AuthenticationProvider {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RowMapper<MemberDetailDto> rowMapper = new RowMapper<MemberDetailDto>() {
        @Override
        public MemberDetailDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new MemberDetailDto(
                        rs.getLong("member_num"),
                        rs.getString("member_id"),
                        rs.getString("member_pw"),
                        rs.getString("member_name"),
                        rs.getString("member_email"),
                        rs.getString("member_image"),
                        rs.getBoolean("member_enabled")
                );
            } catch (Exception e) {
                return null;
            }

        }
    };



    @Value("${cloud.aws.s3.basic-image}")
    private String basicImage;

    @Value("${cloud.aws.s3.bucket}")
    private String s3BucketLink;

    public Long join(Members members) throws Exception {
        validateDuplicateMember(members);
        return members.getMembersId();
    }

    private void validateDuplicateMember(Members member) throws Exception {
        Members findMember = null;
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    //  문자열로 받은 생년월일 값 하나로 합쳐서 LocalDate타입으로 변경
    private LocalDate stringToDate(MemberJoinDto memberJoinDto) {
        String year = memberJoinDto.getMemberBirthYear();
        String month = memberJoinDto.getMemberBirthMonth();
        String date = memberJoinDto.getMemberBirthDate();

        //  1 ~ 9일은 뒤에 0을 붙이도록 함
        if (date.length() < 2) {
            date = String.format("%2s", date).replace(" ", "0");
        }

        String combineBirth = year + "-" + month + "-" + date;

        LocalDate localDate = LocalDate.parse(combineBirth, DateTimeFormatter.ISO_LOCAL_DATE);

        return localDate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("멤버 전달");
        return findById(username);
    }

    public void joinMember(MemberJoinDto memberJoinDto) {
        String id = memberJoinDto.getMemberId();
        String pw = memberJoinDto.getMemberPw();
        String name = memberJoinDto.getMemberName();
        String image = basicImage;
        LocalDate birth = stringToDate(memberJoinDto);
        String gender = memberJoinDto.getMemberGender().getGenderSelect();
        String email = memberJoinDto.getMemberEmail();
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = false;

        String encodedPw = bCryptPasswordEncoder.encode(pw);

        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_birth," +
                                "member_regdate, member_gender, member_email, member_enabled) " +
                                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                                id, encodedPw, name, image, birth, regtime, gender, email, isEnabled);
    }

    public MemberDetailDto findById(String id) {
        String sql = "select * from member where member_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authentication 진입");
        String memberId = authentication.getName();
        String memberPw = authentication.getCredentials().toString();
        String savedPw = findById(memberId).getMemberPw();

        if (bCryptPasswordEncoder.matches(memberPw, savedPw)) {
            log.info("비밀번호 검증 성공");
            // UsernamePasswordAuthenticationToken은 권한까지 부여한 생성자를 사용해야 인증을 해준다.
            return new UsernamePasswordAuthenticationToken(findById(memberId), null, null);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

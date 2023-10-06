package com.todolist.DoToday.service;

import com.todolist.DoToday.jwt.JwtTokenProvider;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService, AuthenticationProvider {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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

    // 현재는 jwtFilter 통과 시 loadUserByUsername을 호출하여 디비를 거치지 않으므로 시큐리티 컨텍스트에는 엔티티 정보를 온전히 가지지 않는다
    // 즉 loadUserByUsername을 호출하는 인증 API를 제외하고는 유저네임, 권한만 가지고 있으므로 Account 정보가 필요하다면 디비에서 꺼내와야함
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

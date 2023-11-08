package com.todolist.DoToday.dao;

import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    protected RowMapper<MemberDetailDto> rowMapper = new RowMapper<MemberDetailDto>() {
        @Override
        public MemberDetailDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new MemberDetailDto(
                        rs.getLong("member_num"),
                        rs.getString("member_id"),
                        rs.getString("member_pw"),
                        rs.getString("member_name"),
                        rs.getString("member_image"),
                        rs.getBoolean("member_enabled")
                );
            } catch (Exception e) {
                return null;
            }

        }
    };

//    public void joinMember(MemberJoinDto memberJoinDto) {
//        String emailDomain = memberJoinDto.getMemberEmailDomain();
//
//        if (StringUtils.hasText(memberJoinDto.getMemberDirectEmail())) {
//            emailDomain = "@" + memberJoinDto.getMemberDirectEmail();
//        }
//
//        String id = memberJoinDto.getMemberId() + emailDomain;
//        String pw = memberJoinDto.getMemberPw();
//        String name = memberJoinDto.getMemberName();
//        String image = basicImage;
//        LocalDate birth = stringToDate(memberJoinDto);
//        String gender = memberJoinDto.getMemberGender().getGenderSelect();
//        LocalDate regtime = LocalDate.now();
//        boolean isEnabled = false;
//
//        String encodedPw = bCryptPasswordEncoder.encode(pw);
//
//        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_birth," +
//                        "member_regdate, member_gender, member_enabled) " +
//                        "values (?, ?, ?, ?, ?, ?, ?, ?)",
//                id, encodedPw, name, image, birth, regtime, gender, isEnabled);
//    }

    public void joinSocialMember(SocialMemberJoinDto socialMemberJoinDto) {
        String memberId = socialMemberJoinDto.getMemberId();
        String encodePw = bCryptPasswordEncoder.encode(socialMemberJoinDto.getMemberPw());
        String memberName = socialMemberJoinDto.getMemberName();
        String memberImage = socialMemberJoinDto.getMemberImage();
        String memberGender = socialMemberJoinDto.getGender();
        LocalDate memberRegdate = socialMemberJoinDto.getRegdate();
        boolean memberExpired = socialMemberJoinDto.isMemberExpired();
        if (memberGender == null) {
            memberGender = "M";
        }

        jdbcTemplate.update("insert into member(member_id, member_pw, member_name, member_image, member_gender, " +
                "member_regdate, member_enabled)" +
                "values (?, ?, ?, ?, ?, ?, ?)", memberId, encodePw, memberName, memberImage, memberGender, memberRegdate,
                memberExpired);
    }

    public MemberDetailDto findById(String id) {
        try {
            String sql = "select * from member where member_id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException error) { // 쿼리문에 해당하는 결과가 없거나 2개 이상일 때
            return null;
        }
    }
}

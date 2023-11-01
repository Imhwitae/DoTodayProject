package com.todolist.DoToday.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.todolist.DoToday.dto.request.KakaoMemberJoinDto;
import com.todolist.DoToday.dto.request.MemberChangePwDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService, AuthenticationProvider {

    private final AmazonS3Client amazonS3Client;
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
    private String s3BucketName;

    @Value("${cloud.aws.s3.bucket-url}")
    private String s3BucketLink;

    @Value("${part.upload.path}")
    private String dirPath;

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
        String emailDomain = memberJoinDto.getMemberEmailDomain();

        if (StringUtils.hasText(memberJoinDto.getMemberDirectEmail())) {
            emailDomain = "@" + memberJoinDto.getMemberDirectEmail();
        }

        String id = memberJoinDto.getMemberId() + emailDomain;
        String pw = memberJoinDto.getMemberPw();
        String name = memberJoinDto.getMemberName();
        String image = basicImage;
        LocalDate birth = stringToDate(memberJoinDto);
        String gender = memberJoinDto.getMemberGender().getGenderSelect();
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = false;

        String encodedPw = bCryptPasswordEncoder.encode(pw);

        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_birth," +
                                "member_regdate, member_gender, member_enabled) " +
                                "values (?, ?, ?, ?, ?, ?, ?, ?)",
                                id, encodedPw, name, image, birth, regtime, gender, isEnabled);
    }

    public void kakaoJoinMember(KakaoMemberJoinDto memberJoinDto) {
        String id = memberJoinDto.getMemberId();
        String name = memberJoinDto.getMemberName();
        String image = memberJoinDto.getMemberImage();
        LocalDate birth = LocalDate.parse(memberJoinDto.getMemberBirthDay(),DateTimeFormatter.ISO_LOCAL_DATE);
        String gender = memberJoinDto.getMemberGender().getGenderSelect();
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = false;

        String encodedPw = bCryptPasswordEncoder.encode("q1w2e3r4t5");
        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_birth," +
                        "member_regdate, member_gender, member_enabled) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?)",
                id, encodedPw, name, image, birth, regtime, gender, isEnabled);
    }

    public MemberDetailDto findById(String id) {
        try {
            String sql = "select * from member where member_id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException error) { // 쿼리문에 해당하는 결과가 없거나 2개 이상일 때
            return null;
        }
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("Authentication 진입");
        log.info("memberId: {}", authentication.getName());
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

    // 이미지 업로드
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        String originalImageName = Normalizer.normalize(multipartFile.getOriginalFilename(), Normalizer.Form.NFC);
        String uid = UUID.randomUUID().toString();  // 고유 uid 생성
        String uploadImageName = "profile_image/" + uid + originalImageName;

        // 폴더 생성
        File folder = new File(dirPath);
        if (!folder.exists()) {
            try {
                folder.mkdir();
                log.info("폴더 생성");
            } catch (Exception e) {
                log.info("폴더가 이미 생성되어있음");
            }
        }

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());  // 이미지 크기
        objectMetadata.setContentType(multipartFile.getContentType());  // 이미지 타입

        amazonS3Client.putObject(s3BucketName, uploadImageName, multipartFile.getInputStream(), objectMetadata);
        log.info("이미지 경로 {}", amazonS3Client.getResourceUrl(s3BucketName, uploadImageName));
        // 폴더 삭제
        folder.delete();

        return amazonS3Client.getResourceUrl(s3BucketName, uploadImageName).toString();
    }

    // 이미지 수정
    public void updateMemberImg(MultipartFile image, String memberId) throws IOException {
        if (!image.isEmpty()) {
            String updateImgUrl = uploadImage(image);
            jdbcTemplate.update("update member set member_image = ? where member_id = ?", updateImgUrl, memberId);
            log.info("{} 유저 이미지 url 변경 {}", memberId, updateImgUrl);
        } else {
            throw new RuntimeException("이미지가 없습니다.");
        }
    }

    // 비밀번호 변경
    public void updateMemberPw(MemberDetailDto memberDetailDto, MemberChangePwDto memberChangePwDto) {
        String memberPrevPw = memberChangePwDto.getMemberPrevPw();
        String memberNewPw = memberChangePwDto.getMemberNewPw();
        String memberConfNewPw = memberChangePwDto.getMemberConfNewPw();
        log.info("기존 비밀번호: {}, " +
                "새 비밀번호: {}, " +
                "새 비밀번호 확인: {}", memberPrevPw, memberNewPw, memberConfNewPw);

        String memberId = memberDetailDto.getMemberId();
        String memberPw = memberDetailDto.getMemberPw();
//        log.info("memberId: {}, " +
//                "memberPw: {}", memberId, memberPw);

        if (bCryptPasswordEncoder.matches(memberPrevPw, memberPw)) {
            if (memberNewPw.equals(memberConfNewPw)) {
                String encodedNewPw = bCryptPasswordEncoder.encode(memberNewPw);
                jdbcTemplate.update("update member set member_pw = ? where member_id = ?", encodedNewPw, memberId);
            } else {
                log.info("새로 입력한 비밀번호가 다름");
            }
        } else {
            log.info("기존 비밀번호가 틀림");
        }

    }
}

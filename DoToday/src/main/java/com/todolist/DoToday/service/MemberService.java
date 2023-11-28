package com.todolist.DoToday.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.todolist.DoToday.api.error.ApiErrorResponse;
import com.todolist.DoToday.api.error.ExceptionEnum;
import com.todolist.DoToday.api.reponse.AcccessTokenApi;
import com.todolist.DoToday.api.reponse.ExceptionDto;
import com.todolist.DoToday.api.reponse.MemberNumDto;
import com.todolist.DoToday.api.reponse.TokensDto;
import com.todolist.DoToday.api.request.ApiMemberJoinDto;
import com.todolist.DoToday.api.request.ApiMemberLoginDto;
import com.todolist.DoToday.dto.MemberTokenDto;
import com.todolist.DoToday.dto.request.KakaoMemberJoinDto;
import com.todolist.DoToday.dto.request.MemberChangePwDto;
import com.todolist.DoToday.dto.request.MemberEtcInfoDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService, AuthenticationProvider {

    private final AmazonS3Client amazonS3Client;
    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private Map<String, Object> apiMap;
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

        return LocalDate.parse(combineBirth, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("멤버 전달");
        return memberMapper.findById(username);
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

        memberMapper.insertMember(id, encodedPw, name, image, birth, regtime, gender, isEnabled);
    }

    public void kakaoJoinMember(KakaoMemberJoinDto memberJoinDto) {
        String id = memberJoinDto.getMemberId();
        String name = memberJoinDto.getMemberName();
        String image = memberJoinDto.getMemberImage();
        LocalDate birth = LocalDate.parse(memberJoinDto.getMemberBirthDay(),DateTimeFormatter.ISO_LOCAL_DATE);
        String gender = memberJoinDto.getMemberGender().getGenderSelect();
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = true;

        String encodedPw = bCryptPasswordEncoder.encode("q1w2e3r4t5");

        memberMapper.insertMember(id, encodedPw, name, image, birth, regtime, gender, isEnabled);
    }

    public MemberDetailDto findById(String id) {
        try {
            return memberMapper.findById(id);
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
            MemberDetailDto member = memberMapper.findById(memberId);
            return new UsernamePasswordAuthenticationToken(member, null, null);
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
            memberMapper.updateImage(updateImgUrl, memberId);
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

        if (bCryptPasswordEncoder.matches(memberPrevPw, memberPw)) {
            if (memberNewPw.equals(memberConfNewPw)) {
                String encodedNewPw = bCryptPasswordEncoder.encode(memberNewPw);
                memberMapper.updatePw(encodedNewPw, memberId);
            } else {
                log.info("새로 입력한 비밀번호가 다름");
            }
        } else {
            log.info("기존 비밀번호가 틀림");
        }
    }

    /*API service*/

    // 앱에서 받아온 정보로 회원가입
    public ResponseEntity<Map<String, Object>> apiMemberJoin(ApiMemberJoinDto apiMemberJoinDto) {
        apiMap = new HashMap<>();

        String id = apiMemberJoinDto.getId();
        String pw = apiMemberJoinDto.getPw();
        String name = apiMemberJoinDto.getName();
        String image = basicImage;
        if (StringUtils.hasText(apiMemberJoinDto.getImage_url()) && !apiMemberJoinDto.getImage_url().equals("null")) {
            image = apiMemberJoinDto.getImage_url();
        }
        LocalDate regtime = LocalDate.now();
        boolean isEnabled = true;

        if (memberMapper.findById(id) != null) {
            apiMap.put("id error", id + " is already in use.");
            return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
        } else {
            String encodedPw = bCryptPasswordEncoder.encode(pw);
            memberMapper.ApiInsertMember(id, encodedPw, name, image, regtime, isEnabled);
            MemberNumDto num = new MemberNumDto(memberMapper.findById(id).getMemberNum());
            apiMap.put("joinSucess", num);
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        }
    }

    // 회원가입 후 추가 정보 입력
    public void inputEtc(MemberJoinDto memberJoinDto) {
        LocalDate birth = stringToDate(memberJoinDto);
        String gender = memberJoinDto.getMemberGender().getGenderSelect();

        memberMapper.insertMemberEtcInfo(birth, gender);
    }

    /* 처음 구현할 때 MemberService가 JwtTokenProvider에도 의존성 주입이 되어있어
       여기서 JwtTokenProvider를 가져다 쓰려고하면 순환 참조 오류 때문에 문제가 됐었다.
       그래서 myBatis로 DB접근 방식을 변경하여 순환 참조 오류를 해결함
       + 그냥 문자열로 Return하면 파싱을 못함!
    */
    public ResponseEntity<Map<String, Object>> checkMemberId(String id) {
        apiMap = new HashMap<>();

        try {
            MemberDetailDto member = memberMapper.findById(id);
            if (member.getMemberId() != null) {
                apiMap.put("error", id + " is already in use.");
                return new ResponseEntity<>(apiMap, HttpStatus.BAD_REQUEST);
            }
        } catch (NullPointerException e) {
            apiMap.put("ok", id + " can be used");
            return new ResponseEntity<>(apiMap, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    public ResponseEntity<Map<String, Object>> apiLogin(ApiMemberLoginDto apiMemberLoginDto) {
//        // 에러 처리하기 + 아이디 중복체크
//        apiMap = new HashMap<>();
//
//        try {
//            MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
//            bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
//        } catch (NullPointerException e) {
//            apiMap.put("error", e.getMessage());
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//        }
//
//        MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
//        boolean checkPw = bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
//        log.info("pw: {}", checkPw);
//
//        if (checkPw) {
//            MemberTokenDto appMemberToken = jwtTokenProvider.createToken(member.getMemberId());
//            apiMap.put("loginSuccess", appMemberToken);
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//        } else {
//            apiMap.put("error", new ResponseEntity<>(HttpStatus.BAD_REQUEST));
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
//        }
//
//    }

    public MemberTokenDto apiLogin(ApiMemberLoginDto apiMemberLoginDto) {
        // 에러 처리하기 + 아이디 중복체크
        apiMap = new HashMap<>();

        try {
            MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
            bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
        } catch (NullPointerException e) {
            apiMap.put("error", e.getMessage());
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
            return null;
        }

        MemberDetailDto member = memberMapper.findById(apiMemberLoginDto.getMemberId());
        boolean checkPw = bCryptPasswordEncoder.matches(apiMemberLoginDto.getMemberPw(), member.getMemberPw());
        log.info("pw: {}", checkPw);

        if (checkPw) {
            MemberTokenDto appMemberToken = jwtTokenProvider.createToken(member.getMemberId());
            apiMap.put("loginSuccess", appMemberToken);
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
            return appMemberToken;
        } else {
            apiMap.put("error", new ResponseEntity<>(HttpStatus.BAD_REQUEST));
//            return new ResponseEntity<>(apiMap, HttpStatus.OK);
            return null;
        }
    }
}

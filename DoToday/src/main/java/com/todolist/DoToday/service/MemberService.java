package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.Gender;
import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.request.MemberLoginDto;
import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final JdbcTemplate jdbcTemplate;

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

        jdbcTemplate.update("insert into member (member_id, member_pw, member_name, member_image, member_birth, member_regdate, " +
                "member_gender, member_email, member_enabled) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", id, pw, name, image, birth, regtime, gender, email, isEnabled);
    }

    public void loginMember(MemberLoginDto memberLoginDto) {

    }



    public void getCodeTest(String code) {
        System.out.println("code = " + code);
    }
}

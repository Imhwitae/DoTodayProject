package com.todolist.DoToday.service;

import com.todolist.DoToday.mapper.MemberMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("멤버 객체가 null이 아니다.")
    void checkMember() {
//        Assertions.assertNotNull(memberMapperRepository.findById("test@gmail.com"));
        Assertions.assertNotNull(memberMapper.findAll());
    }
}

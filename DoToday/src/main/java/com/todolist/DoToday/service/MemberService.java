package com.todolist.DoToday.service;

import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final FirebaseServiceKakaoImpl firebaseServiceKakao;

    public Long join(Members members) throws Exception {
        validateDuplicateMember(members);
        firebaseServiceKakao.insert(members);
        return members.getMembersId();
    }

    private void validateDuplicateMember(Members member) throws Exception {
        Members findMember = firebaseServiceKakao.getDetail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    public void getCodeTest(String code) {
        System.out.println("code = " + code);
    }
}

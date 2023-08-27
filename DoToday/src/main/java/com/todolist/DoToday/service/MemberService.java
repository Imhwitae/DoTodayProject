package com.todolist.DoToday.service;

import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    public Long join(Members members){
        validateDuplicateMember(members);
        return members.getMembersId();
    }

    private void validateDuplicateMember(Members member) {
//        Members findMember = membersRepository.findByEmail(member.getEmail())
//                .orElse(null);;
//        if (findMember != null) {
//            throw new IllegalStateException("이미 가입된 회원입니다.");
//        }
    }
}

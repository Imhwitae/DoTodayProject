package com.todolist.DoToday.controller;

import com.todolist.DoToday.entity.Test;
import com.todolist.DoToday.service.FirebaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FirebaseTestController {

    private final FirebaseServiceImpl firebaseService;

    @PostMapping("/insertMember")
    public String insertMember(@RequestBody Test test) throws Exception{
        System.out.println("컨트롤러 진입");
        return firebaseService.insert(test);
    }

    /**
     * @주소창에: http://localhost:8080/insertMember
     * @확인하기: Firebase Database 가서 값이 들어갔는지 확인
     */
    @GetMapping("/insertMember")
    public void insertMember() {
        firebaseService.insert();
    }

    /**
     * @param id Firestore Database의 문서 이름 복사
     * @주소창에: http://localhost:8080/getMemberDetail?id=id
     */
    @GetMapping("/getMemberDetail")
    public Test getMemberDetail(@RequestParam String id) throws Exception{
        return firebaseService.getDetail(id);
    }

    @GetMapping("/updateMember")
    public String updateMember(@RequestParam Test test) throws Exception{
        return firebaseService.update(test);
    }

    @GetMapping("/deleteMember")
    public String deleteMember(@RequestParam String id) throws Exception{
        return firebaseService.delete(id);
    }

}

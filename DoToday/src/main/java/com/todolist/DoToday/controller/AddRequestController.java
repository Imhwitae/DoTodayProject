package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.service.AddRequestService;
import com.todolist.DoToday.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request")
public class AddRequestController {
    private final AddRequestService addRequestService;
    @GetMapping("/list")
    public String requestList(){
        return "test/request_test";
    }

    @PostMapping("/accept")
    public String requestAccept(){
        return "redirect:/request/list";
    }
}

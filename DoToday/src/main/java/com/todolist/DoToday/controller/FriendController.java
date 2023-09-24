package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("/list")
    public String friendList(Model model){
        List<FriendList> friendLists = friendService.selectFriendList("cor2580");
        model.addAttribute("friendList", friendLists);
        return "test/friendlist_test";
    }


}

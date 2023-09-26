package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;
    private FriendList friendList;

    @GetMapping("/list")
    public String friendList(Model model){
        List<FriendInfoDto> infoDtoList = friendService.info("cor2580");
        model.addAttribute("friendList", infoDtoList);
        return "test/friendlist_test";
    }

    @PostMapping("/delete/{friendId}")
    public String deleteFriend(@PathVariable String friendId){
        friendList.setFriendId(friendId);
        friendList.setUserId("cor2580");
        friendService.deleteFriend(friendList);
        return "redirect:/friend/list";
    }

    @GetMapping("/blockList")
    public String blockList(Model model){
        List<FriendInfoDto> blist = friendService.blockUserList("cor2580");
        model.addAttribute("blockList", blist);
        return "test/block_friendlist_test";
    }

}

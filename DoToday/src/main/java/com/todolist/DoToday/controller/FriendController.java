package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.service.AddRequestService;
import com.todolist.DoToday.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {
    private final FriendService friendService;
    private final AddRequestService addRequestService;

    @GetMapping("/list")
    public String friendList(Model model,@ModelAttribute("friend") FriendList friendList){
        List<FriendInfoDto> infoDtoList = friendService.info("cor2580");
        int listCount = friendService.listCount("cor2580");
        int requestCount = addRequestService.listCount("cor2580");
        model.addAttribute("friendList", infoDtoList);
        model.addAttribute("lCount", listCount);
        model.addAttribute("rCount", requestCount);
        return "test/friendlist_test";
    }

    @PostMapping("/delete")
    public String deleteFriend(@RequestParam("userId") String userId,
                               @RequestParam("friendId") String friendId){
        FriendList friendList = new FriendList();
        friendList.setFriendId(friendId);
        friendList.setUserId(userId);
        friendService.deleteFriend(friendList);
        return "redirect:/friend/list";
    }

    @PostMapping("/apply")
    public String applyFriend(@RequestParam("friendId") String friendId){
        AddRequest add = new AddRequest();
        add.setReceiverId(friendId);
        add.setSenderId("cor2580");
        friendService.addFriend(add);
        return "redirect:/request/search";
    }
}

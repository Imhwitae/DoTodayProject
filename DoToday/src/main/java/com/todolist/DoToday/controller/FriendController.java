package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.AddRequestService;
import com.todolist.DoToday.service.FriendService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
//친구 목록 컨트롤러
public class FriendController {
    private final FriendService friendService;
    private final AddRequestService addRequestService;
    private final JwtTokenProvider jtp;

    //친구 리스트
    @GetMapping("/list")
    public String friendList(HttpServletRequest request,
                             Model model, @ModelAttribute("friend") FriendList friendList){
        Cookie[] cookies = request.getCookies();

        MemberDetailDto mdd = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                String tokenName = cookie.getName();
                String value = cookie.getValue();

                if (tokenName.equals("accessToken")) {
                    mdd = jtp.getMember(value);
                }
            }
        }

        List<FriendInfoDto> infoDtoList = friendService.info(mdd.getMemberId());

        int listCount = friendService.listCount(mdd.getMemberId());
        int requestCount = addRequestService.listCount(mdd.getMemberId());

        model.addAttribute("userDto",mdd);
        model.addAttribute("friendList", infoDtoList);
        model.addAttribute("lCount", listCount);
        model.addAttribute("rCount", requestCount);
        return "friend/friendlist_test";
    }

    //친구 삭제
    @PostMapping("/delete")
    public String deleteFriend(@RequestParam("userId") String userId,
                               @RequestParam("friendId") String friendId){
        FriendList friendList = new FriendList();
        log.info(friendId);
        log.info(userId);
        friendList.setFriendId(friendId);
        friendList.setUserId(userId);
        friendService.deleteFriend(friendList);
        return "redirect:/friend/list";
    }

    //친구 신청 수락
    @PostMapping("/apply")
    public String applyFriend(@RequestParam("friendId") String friendId){
        AddRequest add = new AddRequest();
        add.setReceiverId(friendId);
        add.setSenderId("cor2580");
        friendService.addFriend(add);
        return "redirect:/request/search";
    }
}

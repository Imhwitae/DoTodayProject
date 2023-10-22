package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.BlockListService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/block")
//차단 지목록 컨트롤러
public class BlockListController {
    private final BlockListService blockListService;
    private final JwtTokenProvider jtp;
    
    @GetMapping("/list")
    public String blockList(HttpServletRequest request, Model model){
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

        List<FriendInfoDto> blist = blockListService.blockUserList(mdd.getMemberId());
        int blockListCount = blockListService.bListCount(mdd.getMemberId());
        model.addAttribute("bCount", blockListCount);
        model.addAttribute("blockList", blist);
        return "friend/block_friendlist_test";
    }

    @PostMapping("/delete")
    public String deleteBlockList(HttpServletRequest request,
                                  @RequestParam("bUserId") String bMemberId){
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

        FriendList fl = new FriendList();
        fl.setUserId(mdd.getMemberId());
        fl.setFriendId(bMemberId);
        blockListService.blockListDelete(fl);
        return "redirect:/block/list";
    }


}

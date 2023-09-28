package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.service.AddRequestService;
import com.todolist.DoToday.service.BlockListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockListController {
    private final BlockListService blockListService;
    
    @GetMapping("/list")
    public String blockList(Model model){
        List<FriendInfoDto> blist = blockListService.blockUserList("cor2580");
        int blockListCount = blockListService.bListCount("cor2580");
        model.addAttribute("bCount", blockListCount);
        model.addAttribute("blockList", blist);
        return "test/block_friendlist_test";
    }

    @PostMapping("/delete")
    public String deleteBlockList(@RequestParam("bUserId") String bMemberId){
        FriendList fl = new FriendList();
        fl.setUserId("cor2580");
        fl.setFriendId(bMemberId);
        blockListService.blockListDelete(fl); 
        return "redirect:/block/list";
    }


}

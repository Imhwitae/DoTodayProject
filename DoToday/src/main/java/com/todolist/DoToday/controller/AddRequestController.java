package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.AddRequestService;
import com.todolist.DoToday.service.FriendService;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request")
//친구 신청 목록 컨트롤러
public class AddRequestController {
    private final AddRequestService addRequestService;
    private final MemberService memberService;
    private final FriendService friendService;
    private final JwtTokenProvider jtp;

    //자신에게온 신청 목록 불러오기
    @GetMapping("/list")
    public String requestList(HttpServletRequest request, Model model){
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
        List<FriendInfoDto> requests = addRequestService.selectRequestList(mdd.getMemberId());
        int requestCount = addRequestService.listCount(mdd.getMemberId());

        model.addAttribute("userInfo", mdd);
        model.addAttribute("requests",requests);
        model.addAttribute("rCount", requestCount);
        return "friend/request_test";
    }

    //친구신청 수락
    @PostMapping("/accept")
    public String requestAccept(@RequestParam("friendId") String friendId){
        AddRequest add = new AddRequest();
        add.setSenderId(friendId);
        add.setReceiverId("cor2580");
        addRequestService.acceptRequest(add);
        return "redirect:/request/list";
    }

    //차단
    @PostMapping("/block")
    public String block(@RequestParam("friendId") String friendId){
        AddRequest add = new AddRequest();
        add.setSenderId(friendId);
        add.setReceiverId("cor2580");
        addRequestService.blockUser(add);
        return "redirect:/request/list";
    }

    //거절
    @PostMapping("/denied")
    public String requestDenied(@RequestParam("friendId") String friendId){
        AddRequest add = new AddRequest();
        add.setSenderId(friendId);
        add.setReceiverId("cor2580");
        addRequestService.deniedRequest(add);
        return "redirect:/request/list";
    }

    //유저 검색 페이락
    @GetMapping("/search")
    public String searchForm(Model model){
        model.addAttribute("exist",0);
//        FriendInfoDto fid = new FriendInfoDto();
//        model.addAttribute("memberInfo", fid);
        return "friend/add_form_test";
    }

    //검색
    @PostMapping("/search")
    public String search(@RequestParam("memberId") String memberId, Model model){
        FriendList fl = new FriendList();
        fl.setUserId("cor2580");
        fl.setFriendId(memberId);
        int status = friendService.friendStatus(fl);
        if (status == 1){
            model.addAttribute("status",1);
        } else{
            model.addAttribute("status", 0);
        }
        FriendInfoDto fid = new FriendInfoDto();
        MemberDetailDto detailDto = memberService.findById(memberId);

        fid.setMemberId(detailDto.getMemberId());
        fid.setMemberName(detailDto.getMemberName());
        fid.setMemberImage(detailDto.getMemberImage());

        model.addAttribute("memberInfo", fid);
        model.addAttribute("exist", 1);
        return "test/add_form_test";
    }


}

package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.dto.response.TodoList;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.FriendService;
import com.todolist.DoToday.service.ListService;
import com.todolist.DoToday.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
@Slf4j
public class ListController {
    private final ListService listService;
    private final FriendService friendService;
    private boolean blank, listExist;
    private final JwtTokenProvider jtp;
    private final MemberService memberService;

    //자신의 투두리스트 확인
    @GetMapping("/todolist")
    public String showMyList(HttpServletRequest request,
                           Model model, @ModelAttribute("todoList") TodoList todoList) {
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
        List<TodoList> list = listService.showToday(mdd.getMemberId());

        listExist = listService.emptyList(list);

        LocalDate currentDate = LocalDate.now();
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        model.addAttribute("date", currentDateStr);
        model.addAttribute("list", list);
        model.addAttribute("memberInfo", mdd);
        model.addAttribute("exist", listExist);
        return "list/todolist_user";
    }

    //해당날짜에 작성된 투두리스트 확인
    @GetMapping("/view")
    public String showDateMyList(HttpServletRequest request,
                                 @ModelAttribute("todoList") TodoList todoList, Model model){
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

        if (mdd.getMemberId() == null || todoList == null){
            return "redirect:/list/todolist";
        }

        List<TodoList> list = listService.show(mdd.getMemberId(), todoList.getDate());

        //리스트가 비어있으면 true 있으면 false를 반환
        listExist = listService.emptyList(list);

        //오늘날짜와 받아온 날짜를 비교해 이미 지난 날이면 '날짜가 이미 지났습니다.'라고 받아옴
        String message = listService.dateCheck(todoList.getDate());

        //받아온 날짜를 지정된 형태로 형 변환 하여 다시 값 전달
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate inputDate = LocalDate.parse(todoList.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        String date = inputDate.format(formatter);

        model.addAttribute("date", date);
        model.addAttribute("msg", message);
        model.addAttribute("exist", listExist);
        model.addAttribute("memberInfo", mdd);
        model.addAttribute("list",list);
        return "list/todolist_user";
    }

    //친구가 작성한 투두리스트 보기
    @GetMapping("/{memberId}/todolist")
    public String viewListOfFriend(HttpServletRequest request,
                                   @ModelAttribute("todoList") TodoList todoList,
                                   @PathVariable("memberId") String friendId, Model model){
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

        FriendList friendList = new FriendList();
        friendList.setUserId(mdd.getMemberId());
        friendList.setFriendId(friendId);

        int friendCheck = 0;
        //볼려는 유저와 친구 상태인지 확인
        friendCheck = friendService.friendStatus(friendList);

        if (friendCheck == 0){
            model.addAttribute("error","친구가 아니면 볼 수 없습니다!");
            return "message/error";
        }

        mdd = memberService.findById(friendId);

        List<TodoList> list = listService.showToday(friendId);

        listExist = listService.emptyList(list);

        LocalDate currentDate = LocalDate.now();
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        log.info(mdd.getMemberId());

        model.addAttribute("date", currentDateStr);
        model.addAttribute("list", list);
        model.addAttribute("memberInfo", mdd);
        model.addAttribute("exist", listExist);
        return "list/todolist_friend";
    }

    //친구가 특정날짜에 작성한 투두리스트 보기
    @GetMapping("/{memberId}/view")
    public String viewDateListOfFriend(HttpServletRequest request,
                                       @ModelAttribute("todoList") TodoList todoList,
                                       @PathVariable("memberId") String friendId, Model model){
        log.info(friendId);
        log.info(todoList.getDate());
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

        FriendList friendList = new FriendList();
        friendList.setUserId(mdd.getMemberId());
        friendList.setFriendId(friendId);

        int friendCheck = 0;
        //볼려는 유저와 친구 상태인지 확인
        friendCheck = friendService.friendStatus(friendList);

        if (friendCheck == 0){
            model.addAttribute("error","친구가 아니면 볼 수 없습니다!");
            return "message/error";
        }

        mdd = memberService.findById(friendId);

        List<TodoList> list = listService.show(friendId, todoList.getDate());

        //리스트가 비어있으면 true 있으면 false를 반환
        listExist = listService.emptyList(list);

        //오늘날짜와 받아온 날짜를 비교해 이미 지난 날이면 '날짜가 이미 지났습니다.'라고 받아옴
        String message = listService.dateCheck(todoList.getDate());

        //받아온 날짜를 지정된 형태로 형 변환 하여 다시 값 전달
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        LocalDate inputDate = LocalDate.parse(todoList.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        String date = inputDate.format(formatter);

        model.addAttribute("date", date);
        model.addAttribute("msg", message);
        model.addAttribute("exist", listExist);
        model.addAttribute("memberInfo", mdd);
        model.addAttribute("list",list);

        return "list/todolist_friend";
    }

    //투두리스트 작성
    @PostMapping("/write/{date}")
    public String writeList(@RequestParam("memberId") String memberId,
                            @PathVariable("date") String date,
                            @ModelAttribute("todoList") TodoList todoList,Model model,
                            HttpServletRequest request){
        blank = listService.validate(todoList);

        date = date.replaceAll("[년월]","-").replaceAll("[ 일]","");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        String setDateStr = inputDate.format(formatter);

        todoList.setMemberId(memberId);
        todoList.setDate(setDateStr);
        log.info(setDateStr);
        log.info(memberId);

        if (blank == true){ // todolist의 title이 비어있을때
            model.addAttribute("error","오늘의 할일을 작성해 주세요!");
            return "/message/error";
        }

        listService.write(todoList);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    //작성된 투두리스트 삭제
    @PostMapping("/delete")
    public String deleteList(@RequestParam("listNum") int listNum,
                             HttpServletRequest request){
        log.info(listNum+"");
        listService.delete(listNum);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    //작성된 투두리스트 수정
    @PostMapping("/update")
    public String updateList(@RequestParam("listNum") int listNum,
                             @ModelAttribute("todoList") TodoList todoList,
                             HttpServletRequest request){
        String referer = request.getHeader("Referer");

        blank = listService.validate(todoList);
        if (blank == true){ // todolist의 title이 비어있을때
            return "redirect:"+ referer;
        }
        todoList.setListNum(listNum);
        listService.updateContent(todoList);

//        return "redirect:/list/todolist";
        return "redirect:"+ referer;
    }

    //작성된 투두리스트 완료
    @PostMapping("/complete/{date}")
    public String completeList(@PathVariable("date") String date,
                               @RequestParam("listNum") int listNum,
                               HttpServletRequest request, Model model){
        date = date.replaceAll("[년월]","-").replaceAll("[ 일]","");
        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        LocalDate currentDate = LocalDate.now();

        if (currentDate.isAfter(inputDate) || currentDate.isEqual(inputDate)) { //변수로 받아온 날짜가 오늘날짜와 같거나 과거일때
            listService.updateComplete(listNum);
        }else{
            model.addAttribute("error","아직 지나지 않은 날짜는 완료 할 수 없습니다!");
            return "/message/error";
        }
        log.info(listNum+"");

//        return "redirect:/list/todolist";
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}

package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.dto.response.TodoList;
import com.todolist.DoToday.jwt.JwtTokenProvider;
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
    private boolean blank, listExist;
    private final JwtTokenProvider jtp;

    @GetMapping("/todolist")
    public String showList(HttpServletRequest request, Model model, @ModelAttribute("todoList") TodoList todoList) {
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
        return "list/todolist_test";
    }

    @GetMapping("/view")
    public String ListView(HttpServletRequest request,
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
        return "list/todolist_test";
    }

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
            return "/test/error";
        }

        listService.write(todoList);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @PostMapping("/delete")
    public String deleteList(@RequestParam("listNum") int listNum,
                             HttpServletRequest request){
        log.info(listNum+"");
        listService.delete(listNum);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

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
            return "/test/error";
        }
        log.info(listNum+"");

//        return "redirect:/list/todolist";
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}

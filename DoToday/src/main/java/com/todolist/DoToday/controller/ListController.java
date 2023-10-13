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
    private final MemberService memberService;
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
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy기 MM월 dd일"));

        model.addAttribute("date", currentDateStr);
        model.addAttribute("list", list);
        model.addAttribute("memberInfo", mdd);
        model.addAttribute("exist", listExist);
        return "list/todolist_test";
    }

//    @GetMapping("/view")
//    public String showListTest(Model model,@ModelAttribute("todoList") TodoList todoList){
//        List<TodoList> list = listService.showToday("aa");
//        model.addAttribute("list",list);
//        return "test/todolist_test";
//    }

    @PostMapping("/view")
    public String ListView(@RequestParam("memberId") String memberId,
                           @ModelAttribute("todoList") TodoList todoList, Model model){
        log.info(memberId);
        log.info(todoList.getDate());
        if (memberId == null || todoList == null){
            return "redirect:/list/todolist";
        }
        List<TodoList> list = listService.show(memberId, todoList.getDate());
        MemberDetailDto mdd = memberService.findById(memberId);

        listExist = listService.emptyList(list);

        String message = listService.dateCheck(todoList.getDate());

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

    @PostMapping("/write")
    public String writeList(@RequestParam("memberId") String memberId,
                            @ModelAttribute("todoList") TodoList todoList,Model model){
        blank = listService.validate(todoList);
        if (blank == true){ // todolist의 title이 비어있을때
            model.addAttribute("error","오늘의 할일을 작성해 주세요!");
            return "/test/error";
        }
        listService.write(todoList);
        return "redirect:/list/todolist";
    }

    @PostMapping("/delete")
    public String deleteList(@RequestParam("listNum") int listNum){
        listService.delete(listNum);
        return "redirect:/list/todolist";
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam("listNum") int listNum,
                             @ModelAttribute("todoList") TodoList todoList, Model model){
        model.addAttribute("view",listService.view(listNum));
        return "test/todolist_updateForm_test";
    }

    @PostMapping("/update")
    public String updateList(@RequestParam("listNum") int listNum,
                             @ModelAttribute("todoList") TodoList todoList, Model model){
        blank = listService.validate(todoList);
        if (blank == true){ // todolist의 title이 비어있을때
            return "redirect:/list/todolist";
        }
        todoList.setListNum(listNum);
        listService.updateContent(todoList);
        return "redirect:/list/todolist";
    }

    @PostMapping("/complete")
    public String completeList(@RequestParam("listNum") int listNum){
        listService.updateComplete(listNum);
        return "redirect:/list/todolist";
    }
}

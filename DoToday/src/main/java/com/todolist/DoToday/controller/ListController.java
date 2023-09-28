package com.todolist.DoToday.controller;

import com.todolist.DoToday.dto.response.TodoList;
import com.todolist.DoToday.service.ListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
@Slf4j
public class ListController {
    private final ListService listService;
    private boolean blank;

    @GetMapping("/view")
    public String showList(Model model,@ModelAttribute("todoList") TodoList todoList){
        List<TodoList> list = listService.showToday("aa");
        model.addAttribute("list",list);
        return "test/todolist_test";
    }

    @PostMapping("/view")
    public String ListView(@ModelAttribute("todoList") TodoList todoList, Model model){
        List<TodoList> list = listService.show(todoList.getMemberId(), todoList.getDate());
        model.addAttribute("list",list);
        return "test/todolist_test";
    }

    @PostMapping("/write")
    public String writeList(@ModelAttribute("todoList") TodoList todoList,Model model){
        blank = listService.validate(todoList);
        if (blank == true){ // todolist의 title이 비어있을때
            model.addAttribute("error","오늘의 할일을 작성해 주세요!");
            return "/test/error";
        }
        listService.write(todoList);
        return "redirect:/list/view";
    }

    @PostMapping("/delete")
    public String deleteList(@RequestParam("listNum") int listNum){
        listService.delete(listNum);
        return "redirect:/list/view";
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
            return "redirect:/list/view";
        }
        todoList.setListNum(listNum);
        listService.updateContent(todoList);
        return "redirect:/list/view";
    }

    @PostMapping("/complete")
    public String completeList(@RequestParam("listNum") int listNum){
        listService.updateComplete(listNum);
        return "redirect:/list/view";
    }
}

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
        listService.write(todoList);
        List<TodoList> list = listService.showToday("aa");
        model.addAttribute("list",list);
        return "redirect:/list/view";
    }

    @PostMapping("/delete/{listNum}")
    public String deleteList(@PathVariable("listNum") int listNum, Model model){
        listService.delete(listNum);
        List<TodoList> list = listService.showToday("aa");
        model.addAttribute("list",list);
        return "redirect:/list/view";
    }

    @GetMapping("/update/{listNum}")
    public String updateList(@PathVariable("listNum") int listNum, @ModelAttribute("todoList") TodoList todoList){
        return "test/todolist_updateForm_test";
    }
}

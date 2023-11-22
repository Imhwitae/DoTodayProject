package com.todolist.DoToday.api;

import com.todolist.DoToday.dto.request.AppListGetDto;
import com.todolist.DoToday.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class ListApiController {
    private final ListService listService;
    private boolean tBlank, wBlank;

    @PostMapping("/write")
    public int appListWrite(@RequestBody AppListGetDto listGetDto){
        tBlank = listService.appTitleValidate(listGetDto);
        if (tBlank == true){ // todolist의 title 비어있을때
            listGetDto.setListTitle("");
        }

        wBlank = listService.appWhenValidate(listGetDto);
        if (wBlank == true){
            listGetDto.setWhenToDo("아무때나");
        }

        String date = listGetDto.getDate();
        date = date.replaceAll("[년월]","-").replaceAll("[ 일]","");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        String setDateStr = inputDate.format(formatter);

        listGetDto.setDate(setDateStr);

        return listService.appListWrite(listGetDto);
    }



//    public String writeList(Model model, HttpServletRequest request){
//        tBlank = listService.titleValidate(todoList);
//        wBlank = listService.whenValidate(todoList);
//
//        date = date.replaceAll("[년월]","-").replaceAll("[ 일]","");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
//        String setDateStr = inputDate.format(formatter);
//
//        todoList.setMemberId(memberId);
//        todoList.setDate(setDateStr);
//        log.info(setDateStr);
//        log.info(memberId);
//
//        if (tBlank == true){ // todolist의 title 비어있을때
//            model.addAttribute("error","오늘의 할일을 작성해 주세요!");
//            return "/message/error";
//        }
//        if (wBlank == true){
//            todoList.setWhenToDo("아무때나");
//        }
//
//        listService.write(todoList);
//
//        String referer = request.getHeader("Referer");
//        return "redirect:"+ referer;
//    }
}

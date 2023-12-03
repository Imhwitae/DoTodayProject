package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.message.Message;
import com.todolist.DoToday.api.reponse.AppListsOfMemberDto;
import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.request.AppListGetDto;
import com.todolist.DoToday.api.request.AppListNumDto;
import com.todolist.DoToday.api.request.RequestAccessToken;
import com.todolist.DoToday.api.service.ListApiService;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import com.todolist.DoToday.service.ListService;
import com.todolist.DoToday.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class ListApiController {
    private final ListApiService listService;
    private boolean tBlank, wBlank;
    @GetMapping("/show")
    public AppListsOfMemberDto showList(@RequestBody RequestAccessToken token){
        log.info(token.getAccessToken());
//        String memberId = jtp.getMemberIdFromToken(token.getAccessToken());
//        AppListsOfMemberDto listDto = new AppListsOfMemberDto();
//
//        List<AppListDto> todoLists = listService.appShowLists(memberId, currentDateStr);
//
//        listDto.setList(todoLists);
//        listDto.setDate(currentDateStr);
//        listDto.setToken("token");
//
//        return listDto;
        LocalDate currentDate = LocalDate.now();
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

        AppListsOfMemberDto listDto = listService.appShowLists(token.getAccessToken(),currentDateStr);

        return listDto;
    }

    @PostMapping("/write")
    public ResponseEntity<Message> appListWrite(@RequestBody AppListGetDto listGetDto) {
        tBlank = listService.appTitleValidate(listGetDto);
        if (tBlank == true) { // todolist의 title 비어있을때
            listGetDto.setListTitle("");
        }

        wBlank = listService.appWhenValidate(listGetDto);
        if (wBlank == true) {
            listGetDto.setWhenToDo("");
        }

        //날짜를 받아올때 0000년 00월 00일로 받을 경우 포멧을 바꿔준다. 아닐경우 사용안함
//        String date = listGetDto.getDate();
//        date = date.replaceAll("[년월]","-").replaceAll("[ 일]","");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
//        String setDateStr = inputDate.format(formatter);
//
//        listGetDto.setDate(setDateStr);

        Message message = new Message();

        if (listGetDto.getType().equals("write")) {// 리스트 작성을 하는 경우
            return listService.appListWrite(listGetDto);
        } else if (listGetDto.getType().equals("update")) {// 리스트 수정을 하는 경우
            return listService.appListUpdate(listGetDto);
        } else {
            message.setMessage("listStatus is null");
            message.setMessage(listGetDto.getType());
            message.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Message> appListDelete(@RequestBody AppListNumDto listNumDto){
        return listService.appListDelete(listNumDto);
    }

    @PostMapping("/complete")
    public ResponseEntity<Message> appListComplete(@RequestBody AppListNumDto listNumDto){
        return listService.appUpdateComplete(listNumDto);
    }

    @PostMapping("/incomplete")
    public ResponseEntity<Message> appListIncomplete(@RequestBody AppListNumDto listNumDto){
        return listService.appUpdateInComplete(listNumDto);
    }

}

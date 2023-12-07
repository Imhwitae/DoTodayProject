package com.todolist.DoToday.api.controller;

import com.todolist.DoToday.api.message.ListsOfMemberMessage;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Tag(name = "리스트 API", description = "리스트 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/list")
public class ListApiController {
    private final ListApiService listService;
    private final JwtTokenProvider jwtTokenProvider;
    private boolean tBlank, wBlank;

    @Operation(summary = "로그인후 리스트 조회", description = "받아온 토큰의 회원아이디로 DB에서 리스트 조회")
    @GetMapping("/show")
    public ResponseEntity<ListsOfMemberMessage> showList(@RequestHeader("Authorization") String token){
        log.info(token);
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

        ResponseEntity<ListsOfMemberMessage> listDto = listService.appShowLists(token,currentDateStr);
        return listDto;
    }

    @Operation(summary = "리스트 작성", description = "받아온 객체의 type 값을 확인해 write의 경우 DB에 삽입, update의 경우 DB데이터 수정")
    @PostMapping("/write")
    public ResponseEntity<Message> appListWrite(@RequestHeader("Authorization") String token,
                                                @RequestBody AppListGetDto listGetDto) {
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
            return listService.appListWrite(token,listGetDto);
        } else if (listGetDto.getType().equals("update")) {// 리스트 수정을 하는 경우
            return listService.appListUpdate(token,listGetDto);
        } else {
            message.setMessage("listStatus is null");
            message.setMessage(listGetDto.getType());
            message.setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "리스트 삭제", description = "리스트 번호를 받아와 리스트 삭제")
    @PostMapping("/delete")
    public ResponseEntity<Message> appListDelete(@RequestHeader("Authorization") String token,
                                                 @RequestBody AppListNumDto listNumDto){
        return listService.appListDelete(token, listNumDto);
    }

    @Operation(summary = "리스트 완료 처리", description = "리스트 번호를 받아와 리스트 완료처리")
    @PostMapping("/complete")
    public ResponseEntity<Message> appListComplete(@RequestHeader("Authorization") String token,
                                                   @RequestBody AppListNumDto listNumDto){
        return listService.appUpdateComplete(token, listNumDto);
    }

    @Operation(summary = "리스트 완료 취소", description = "리스트 번호를 받아와 리스트 완료 취소")
    @PostMapping("/incomplete")
    public ResponseEntity<Message> appListIncomplete(@RequestHeader("Authorization") String token,
                                                     @RequestBody AppListNumDto listNumDto){
        return listService.appUpdateInComplete(token, listNumDto);
    }

}

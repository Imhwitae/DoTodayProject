package com.todolist.DoToday.api.service;

import com.todolist.DoToday.api.error.ApiErrorResponse;
import com.todolist.DoToday.api.message.Message;
import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.reponse.AppListsOfMemberDto;
import com.todolist.DoToday.api.request.AppListGetDto;
import com.todolist.DoToday.api.request.AppListNumDto;
import com.todolist.DoToday.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListApiService {
    private final JwtTokenProvider jwt;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<AppListDto> appListRowMapper = new RowMapper<AppListDto>() {
        @Override
        public AppListDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppListDto todoList = new AppListDto();
            todoList.setListTitle(rs.getString("list_title"));
            todoList.setComplete(rs.getInt("complete"));
            todoList.setListNum(rs.getInt("list_num"));
//            todoList.setMemberId(rs.getString("member_id"));
            todoList.setWhenToDo(rs.getString("when_todo"));
//            todoList.setDate(rs.getString("write_date"));
            return todoList;
        }
    };

    public boolean appTitleValidate(AppListGetDto listGetDto){
        String title = listGetDto.getListTitle();
        boolean blank = false;
        if (title.trim().isEmpty() == true || title == null){ //앞뒤로 공백이 있으면 지워줌
            blank = true;
        }
        return blank;
    }

    public boolean appWhenValidate(AppListGetDto listGetDto){
        String title = listGetDto.getWhenToDo();
        boolean blank = false;
        if (title.trim().isEmpty() == true || title == null){ //앞뒤로 공백이 있으면 지워줌
            blank = true;
        }
        return blank;
    }

    public AppListsOfMemberDto appShowLists(String token, String date) {
        AppListsOfMemberDto dto = new AppListsOfMemberDto();
        String memberId;
        if (!jwt.validateToken(token)) {//토큰이 유효하지 않을때
            dto.setList(null);
            dto.setAccessToken("Invalid_Token");
            dto.setDate(null);
        } else {
            memberId = jwt.getMemberIdFromToken(token);
            String sql = "select * from list where member_id = ? and write_date = ?";
            List<AppListDto> list = jdbcTemplate.query(sql, new Object[]{memberId, date}, appListRowMapper);

            if (list.isEmpty()) {
                list = null;
            }

            dto.setDate(date);
            dto.setList(list);
            dto.setAccessToken(token);
        }
        return dto;
    }

    public ResponseEntity<Message> appListWrite(AppListGetDto listGetDto){
        Message message = new Message();
        String memberId;
        if (!jwt.validateToken(listGetDto.getAccessToken())){
            message.setMessage("Invalid_Token");
            message.setStatus(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
        }
        memberId = jwt.getMemberIdFromToken(listGetDto.getAccessToken());

        if (listGetDto.getDate() != null){
            jdbcTemplate.update("insert into list(list_title,member_id,write_date,when_todo) values(?,?,?,?)"
                    , listGetDto.getListTitle(), memberId, listGetDto.getDate(), listGetDto.getWhenToDo());
        }else {
            jdbcTemplate.update("insert into list(list_title,member_id,when_todo) values(?,?,?)"
                    , listGetDto.getListTitle(), memberId, listGetDto.getWhenToDo());
        }
        message.setMessage("Write_Success");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    public ResponseEntity<Message> appListUpdate(AppListGetDto listGetDto){
        Message message = new Message();

        if (!jwt.validateToken(listGetDto.getAccessToken())){
            message.setMessage("Invalid_Token");
            message.setStatus(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
        }

        jdbcTemplate.update("update list set list_title = ? , when_todo = ? where list_num = ?"
                , listGetDto.getListTitle(), listGetDto.getWhenToDo(), listGetDto.getListNum());
        message.setMessage("Update_success");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //완료처리
    public ResponseEntity<Message> appUpdateComplete(AppListNumDto listDto){
        Message message = new Message();

        if (!jwt.validateToken(listDto.getAccessToken())){
            message.setMessage("Invalid_Token");
            message.setStatus(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
        }
        jdbcTemplate.update("update list set complete = 1 where list_num = ?"
                , listDto.getListNum());
        message.setMessage("CompleteUpdate_success");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //미완료처리
    public ResponseEntity<Message> appUpdateInComplete(AppListNumDto listDto){
        Message message = new Message();

        if (!jwt.validateToken(listDto.getAccessToken())){
            message.setMessage("Invalid_Token");
            message.setStatus(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
        }
        jdbcTemplate.update("update list set complete = 0 where list_num = ?"
                , listDto.getListNum());
        message.setMessage("InCompleteUpdate_success");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    //삭제
    public ResponseEntity<Message> appListDelete(AppListNumDto listDto){
        Message message = new Message();

        if (!jwt.validateToken(listDto.getAccessToken())){
            message.setMessage("Invalid_Token");
            message.setStatus(HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(message,HttpStatus.UNAUTHORIZED);
        }
        int result = jdbcTemplate.update("delete from list where list_num = ?"
                ,listDto.getListNum());
        message.setMessage("ListDelete_success");
        message.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


}

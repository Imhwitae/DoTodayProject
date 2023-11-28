package com.todolist.DoToday.api.service;

import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.request.AppListGetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service

@RequiredArgsConstructor
public class ListApiService {
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

    public List<AppListDto> appShowLists(String userId, String date){
        String sql = "select * from list where member_id = ? and write_date = ?";

        List<AppListDto> list = jdbcTemplate.query(sql, new Object[]{userId,date}, appListRowMapper);

        if (list.isEmpty()){
            list = null;
        }
        return list;
    }

    public int appListWrite(AppListGetDto listGetDto){
        int result = 0;
        if (listGetDto.getDate() != null){
            result = jdbcTemplate.update("insert into list(list_title,member_id,write_date,when_todo) values(?,?,?,?)"
                    , listGetDto.getListTitle(), listGetDto.getMemberId(), listGetDto.getDate(), listGetDto.getWhenToDo());
        }else {
            result = jdbcTemplate.update("insert into list(list_title,member_id,when_todo) values(?,?,?)"
                    , listGetDto.getListTitle(), listGetDto.getMemberId(), listGetDto.getWhenToDo());
        }
        return result;
    }

    public int appListUpdate(AppListGetDto listGetDto){
        int result = 0;
        result = jdbcTemplate.update("update list set list_title = ? and when_todo = ? where list_num = ?"
                , listGetDto.getListTitle(), listGetDto.getWhenToDo(), listGetDto.getListNum());
        return result;
    }

    //완료처리
    public int appUpdateComplete(int num){
        int result = jdbcTemplate.update("update list set complete = 1 where list_num = ?"
                , num);
        return result;
    }

    //미완료처리
    public int appUpdateInComplete(int num){
        int result = jdbcTemplate.update("update list set complete = 0 where list_num = ?"
                , num);
        return result;
    }

    //삭제
    public int appListDelete(int listNum){
        int result = jdbcTemplate.update("delete from list where list_num = ?"
                ,listNum);
        return result;
    }
}

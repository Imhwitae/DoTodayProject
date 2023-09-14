package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.response.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class ListService {
    private JdbcTemplate jdbcTemplate;

    public ListService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //할일 작성
    public void write(List list){
        jdbcTemplate.update("insert into list(list_title,member_id) values(?,?)"
                , list.getListTitle(), list.getMemberId());
    }

    //삭제
    public void delete(List list){
        jdbcTemplate.update("delete list where list_num = ?"
                , list.getListNum());
    }

    //수정
    public void updateContent(List list){
        jdbcTemplate.update("update list set list_title = ? where list_num = ?"
                , list.getListTitle(), list.getListNum());
    }

    //완료처리
    public void updateComplete(List list){
        jdbcTemplate.update("update list set complete = ? where list_num = ?"
                , list.getComplete(), list.getListNum());
    }
}

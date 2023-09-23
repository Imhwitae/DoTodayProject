package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.response.TodoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@Slf4j
public class ListService {
    private JdbcTemplate jdbcTemplate;

    public ListService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<TodoList> show(String userId,String date){
        String sql = "select * from list where member_id = ? and write_date = ?";

        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{userId,date},
                new RowMapper<TodoList>() {
                    @Override
                    public TodoList mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TodoList todoList = new TodoList();
                        todoList.setListTitle(rs.getString("list_title"));
                        todoList.setComplete(rs.getInt("complete"));
                        todoList.setListNum(rs.getInt("list_num"));
                        todoList.setMemberId(rs.getString("member_id"));
                        return todoList;
                    }
                }
            );
        return list;
    }

    public List<TodoList> view(int listNum){
        String sql = "select * from list where list_num = ?";

        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{listNum},
                new RowMapper<TodoList>() {
                    @Override
                    public TodoList mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TodoList todoList = new TodoList();
                        todoList.setListTitle(rs.getString("list_title"));
                        todoList.setComplete(rs.getInt("complete"));
                        todoList.setListNum(rs.getInt("list_num"));
                        todoList.setMemberId(rs.getString("member_id"));
                        return todoList;
                    }
                }
        );
        return list;
    }

    public List<TodoList> showToday(String userId){
        String sql = "select * from list where member_id = ? and write_date = curdate()";

        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{userId},
                new RowMapper<TodoList>() {
                    @Override
                    public TodoList mapRow(ResultSet rs, int rowNum) throws SQLException {
                        TodoList todoList = new TodoList();
                        todoList.setListTitle(rs.getString("list_title"));
                        todoList.setComplete(rs.getInt("complete"));
                        todoList.setListNum(rs.getInt("list_num"));
                        todoList.setMemberId(rs.getString("member_id"));
                        return todoList;
                    }
                }
        );
        return list;
    }

    //할일 작성
    public int write(TodoList todoList){
        int result = jdbcTemplate.update("insert into list(list_title,member_id) values(?,?)"
                , todoList.getListTitle(), todoList.getMemberId());
        return result;
    }

    //삭제
    public int delete(int listNum){
        int result = jdbcTemplate.update("delete from list where list_num = ?"
                ,listNum);
        return result;
    }

    //수정
    public int updateContent(TodoList todoList){
        int result = 0;
        result = jdbcTemplate.update("update list set list_title = ? where list_num = ?"
                , todoList.getListTitle(), todoList.getListNum());
        return result;
    }

    //완료처리
    public int updateComplete(int num){
        int result = jdbcTemplate.update("update list set complete = 1 where list_num = ?"
                , num);
        return result;
    }

    public boolean validate(TodoList todoList){
        String title = todoList.getListTitle();
        boolean blank = false;
        if (title.trim().isEmpty() == true || title == null){//앞뒤로 공백이 있으면 지워줌
            blank = true;
        }
        return blank;
    }
}

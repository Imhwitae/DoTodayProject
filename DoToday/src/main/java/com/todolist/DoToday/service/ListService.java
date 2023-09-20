package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.response.TodoList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
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
    public void write(TodoList todoList){
        jdbcTemplate.update("insert into list(list_title,member_id) values(?,?)"
                , todoList.getListTitle(), todoList.getMemberId());
    }

    //삭제
    public void delete(int listNum){
        jdbcTemplate.update("delete from list where list_num = ?"
                ,listNum);
    }

    //수정
    public void updateContent(TodoList todoList){
        jdbcTemplate.update("update list set list_title = ? where list_num = ?"
                , todoList.getListTitle(), todoList.getListNum());
    }

    //완료처리
    public void updateComplete(int num){
        jdbcTemplate.update("update list set complete = 1 where list_num = ?"
                , num);
    }
}

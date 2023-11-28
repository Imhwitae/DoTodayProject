package com.todolist.DoToday.service;

import com.todolist.DoToday.api.reponse.AppListDto;
import com.todolist.DoToday.api.request.AppListGetDto;
import com.todolist.DoToday.dto.response.TodoList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListService {
    private final JdbcTemplate jdbcTemplate;

//    public ListService(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    private final RowMapper<TodoList> listRowMapper = new RowMapper<TodoList>() {
        @Override
        public TodoList mapRow(ResultSet rs, int rowNum) throws SQLException {
            TodoList todoList = new TodoList();
            todoList.setListTitle(rs.getString("list_title"));
            todoList.setComplete(rs.getInt("complete"));
            todoList.setListNum(rs.getInt("list_num"));
            todoList.setMemberId(rs.getString("member_id"));
            todoList.setWhenToDo(rs.getString("when_todo"));
            todoList.setDate(rs.getString("write_date"));
            return todoList;
        }
    };

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

    public List<TodoList> show(String userId,String date){
        String sql = "select * from list where member_id = ? and write_date = ?";

        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{userId,date}, listRowMapper);
        return list;
    }

    public List<TodoList> view(int listNum){
        String sql = "select * from list where list_num = ?";
        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{listNum}, listRowMapper);
        return list;
    }

    public List<TodoList> showToday(String userId){
        String sql = "select * from list where member_id = ? and write_date = curdate()";

        List<TodoList> list = jdbcTemplate.query(sql, new Object[]{userId}, listRowMapper);
        return list;
    }

    //할일 작성
    public int write(TodoList todoList){
        int result = 0;
        if (todoList.getDate() != null){
            result = jdbcTemplate.update("insert into list(list_title,member_id,write_date,when_todo) values(?,?,?,?)"
                    , todoList.getListTitle(), todoList.getMemberId(), todoList.getDate(), todoList.getWhenToDo());
        }else {
            result = jdbcTemplate.update("insert into list(list_title,member_id,when_todo) values(?,?,?)"
                    , todoList.getListTitle(), todoList.getMemberId(), todoList.getWhenToDo());
        }
        return result;
    }


    //삭제
    public int delete(int listNum){
        int result = jdbcTemplate.update("delete from list where list_num = ?"
                ,listNum);
        return result;
    }

    //수정
    public int updateWhenTodo(TodoList todoList){
        int result = 0;
        result = jdbcTemplate.update("update list set when_todo = ? where list_num = ?"
                , todoList.getWhenToDo(), todoList.getListNum());
        return result;
    }

    //수정
    public int updateTitle(TodoList todoList){
        int result = 0;
        result = jdbcTemplate.update("update list set list_title = ? where list_num = ?"
                , todoList.getListTitle(), todoList.getListNum());
        return result;
    }

    //수정
    public int updateAll(TodoList todoList){
        int result = 0;
        result = jdbcTemplate.update("update list set list_title = ? , when_todo = ? where list_num = ?"
                , todoList.getListTitle(), todoList.getWhenToDo(), todoList.getListNum());
        return result;
    }

    //완료처리
    public int updateComplete(int num){
        int result = jdbcTemplate.update("update list set complete = 1 where list_num = ?"
                , num);
        return result;
    }

    //미완료처리
    public int updateInComplete(int num){
        int result = jdbcTemplate.update("update list set complete = 0 where list_num = ?"
                , num);
        return result;
    }

    public boolean titleValidate(TodoList todoList){
        String title = todoList.getListTitle();
        boolean blank = false;
        if (title.trim().isEmpty() == true || title == null){ //앞뒤로 공백이 있으면 지워줌
            blank = true;
        }
        return blank;
    }

    public boolean whenValidate(TodoList todoList){
        String title = todoList.getWhenToDo();
        boolean blank = false;
        if (title.trim().isEmpty() == true || title == null){ //앞뒤로 공백이 있으면 지워줌
            blank = true;
        }
        return blank;
    }

    public String dateCheck(String date){
        String msg = null;

        LocalDate currentDate = LocalDate.now(); //현재 날짜 호출
        String currentDateStr = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); //호출한 날짜 형식을 입력받은 모양으로 변경

        LocalDate inputDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE); //입력받은 날짜를 타입 변환을 시켜줌

        log.info(inputDate+"");
        log.info(currentDateStr);

//        if (date.trim().replace(" ","") != currentDateStr.trim().replace(" ","")){
//            msg = "날짜가 이미 지났습니다.";
//        }
        if (currentDate.isAfter(inputDate)){
            msg = "날짜가 이미 지났습니다.";
        }

        log.info(msg);

        return msg;
    }

    public boolean emptyList(List<TodoList> list){
        boolean listExist;
        if (list.isEmpty()){ //투두 리스트가 비어있는지 확인
            listExist = false;
        }else{
            listExist = true;
        }
        return listExist;
    }

}

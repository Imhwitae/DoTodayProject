package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.response.FriendList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class FriendService {
    private JdbcTemplate jdbcTemplate;

    public FriendService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<FriendList> selectFriendList(String userId){
        String sql = "select * from friend_list where user_id = ?"; //친구목록 조회하는 쿼리문
        List<FriendList> list =jdbcTemplate.query(sql,new Object[] {userId},//query()에서 sql 바인딩변수를 배열로 받음 (지정해야될 값이 여러개일수도 있기때문)
                new RowMapper<FriendList>(){
                    public FriendList mapRow(ResultSet rs,int rowNum) throws SQLException{//ResultSet에 값을 담고 FreindList에 담는걸 rowNum만큼 한다
                        FriendList friendList = new FriendList();
                        friendList.setUserId(rs.getString("user_id"));
                        friendList.setFriendId(rs.getString("friend_id"));

                        return friendList;
                    }
                }
            );
        return list;
    }

    public int deleteFriend(FriendList friendList){
        String sql = "delete from friend_list where friend_id = ? and user_id = ?";
        int result = jdbcTemplate.update(sql,friendList.getFriendId(),friendList.getUserId());

        return result;
    }
}
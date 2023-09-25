package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
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
    private final JdbcTemplate jdbcTemplate;

    public FriendService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //친구 목록 불러오기
    public List<FriendList> selectFriendList(String userId){
        String sql = "select * from friend_list where user_id = ?"; //친구목록 조회하는 쿼리문
        List<FriendList> list =jdbcTemplate.query(sql,new Object[] {userId},//query()에서 sql 바인딩변수를 배열로 받음 (지정해야될 값이 여러개일수도 있기때문)
                new RowMapper<FriendList>(){
                    @Override
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

    //친구 삭제
    public int deleteFriend(FriendList friendList){
        String sql = "delete from friend_list where friend_id = ? and user_id = ?";
        jdbcTemplate.update(sql,friendList.getUserId(),friendList.getFriendId()); //상대방 리스트에서 삭제
        int result = jdbcTemplate.update(sql,friendList.getFriendId(),friendList.getUserId());//본인 리스트에서 삭가

        return result;
    }

    //차단된 유저 리스트
    public List<AddRequest> blockUserList(String userId){
        String sql = "select * from add_request where receiver_id = ? and req_status = 0";
        List<AddRequest> list = jdbcTemplate.query(sql, new Object[]{userId},
                new RowMapper<AddRequest>() {
                    @Override
                    public AddRequest mapRow(ResultSet rs,int rowNum) throws SQLException{
                        AddRequest addRequest = new AddRequest();
                        addRequest.setSenderId(rs.getString("sender_id"));
                        return addRequest;
                    }
                }
            );
        return list;
    }

    //친구 신청
    public int addFriend(AddRequest addRequest){
        String sql = "insert into add_request(receiver_id, sender_id) values (?,?)";
        int result = jdbcTemplate.update(sql, addRequest.getReceiverId(), addRequest.getSenderId());
        return result;
    }

    //친구정보 가져오기
    public List<FriendInfoDto> infoList(String memberId){
        String sql = "select * from friend_list where user_id = ?";
        List<FriendInfoDto> list = jdbcTemplate.query(sql, new Object[]{memberId},
                new RowMapper<FriendInfoDto>() {
                    @Override
                    public FriendInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        String userId = "";
                        FriendInfoDto friendInfoDto = new FriendInfoDto();

                        return null;
                    }
                }
            );
        return list;
    }
}

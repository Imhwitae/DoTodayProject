package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import com.todolist.DoToday.entity.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final JdbcTemplate jdbcTemplate;
    private final MemberService memberService;

//    public FriendService(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }

    //친구정보 가져오기(이름 사진 아이디)
    public List<FriendInfoDto> info(String memberId){
        String sql = "select * from friend_list where user_id = ?";
        List<FriendInfoDto> flist = jdbcTemplate.query(sql, new Object[]{memberId},
                new RowMapper<FriendInfoDto>() {
                    @Override
                    public FriendInfoDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                        MemberDetailDto md = memberService.findById(rs.getString("friend_id"));
                        FriendInfoDto fid = new FriendInfoDto();
                        fid.setMemberId(md.getMemberId());
                        fid.setMemberName(md.getMemberName());
                        fid.setMemberImage(md.getMemberImage());
                        return fid;
                    }
                }
        );
        return flist;
    }

    //친구상태
    public int friendStatus(FriendList friendList){
        String sql = "select count(*) from friend_list where friend_id = ? and user_id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, friendList.getFriendId(), friendList.getUserId());
        return result;
    }


    //친구 삭제
    public int deleteFriend(FriendList friendList){
        String sql = "delete from friend_list where friend_id = ? and user_id = ?";
        jdbcTemplate.update(sql,friendList.getUserId(),friendList.getFriendId()); //상대방 리스트에서 삭제
        int result = jdbcTemplate.update(sql,friendList.getFriendId(),friendList.getUserId());//본인 리스트에서 삭가
        return result;
    }

    //친구 신청
    public int addFriend(AddRequest addRequest){
        String sql = "insert into add_request(receiver_id, sender_id) values (?,?)";
        int result = jdbcTemplate.update(sql, addRequest.getReceiverId(), addRequest.getSenderId());
        return result;
    }

    //친구리스트 개수
    public int listCount(String userId) {
        String sql = "select count(*) from friend_list where user_id = ?";
        int result = this.jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return result;
    }
}

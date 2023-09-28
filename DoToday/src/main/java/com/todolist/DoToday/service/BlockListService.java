package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.request.AddRequest;
import com.todolist.DoToday.dto.request.FriendInfoDto;
import com.todolist.DoToday.dto.response.FriendList;
import com.todolist.DoToday.dto.response.MemberDetailDto;
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
public class BlockListService {
    private final JdbcTemplate jdbcTemplate;
    private final MemberService memberService;

//    public BlockListService(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }


    //차단된 유저 리스트
    public List<FriendInfoDto> blockUserList(String userId){
        String sql = "select * from add_request where receiver_id = ? and req_status = 0";
        List<FriendInfoDto> list = jdbcTemplate.query(sql, new Object[]{userId},
                new RowMapper<FriendInfoDto>() {
                    @Override
                    public FriendInfoDto mapRow(ResultSet rs,int rowNum) throws SQLException{
                        MemberDetailDto md = memberService.findById(rs.getString("sender_id"));
                        FriendInfoDto fid = new FriendInfoDto();
                        fid.setMemberImage(md.getMemberImage());
                        fid.setMemberId(md.getMemberId());
                        fid.setMemberName(md.getMemberName());
                        return fid;
                    }
                }
            );
        return list;
    }

    //차단 삭제
    public int blockListDelete(FriendList friendList){
        String sql = "delete from add_request where receiver_id = ? and sender_id = ? and req_status = 0";
        int result = jdbcTemplate.update(sql, friendList.getUserId(), friendList.getFriendId());//본인 리스트에서 삭제

        return result;
    }

    //차단리스트 개수
    public int bListCount(String userId) {
        String sql = "select count(*) from add_request where receiver_id = ? and req_status = 0";
        int result = this.jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return result;
    }
}

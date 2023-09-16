package com.todolist.DoToday.service;

import com.todolist.DoToday.dto.request.AddRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class AddRequestService {
    private JdbcTemplate jdbcTemplate;

    public AddRequestService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<AddRequest> selectRequestList(String userId){
        String sql = "select * from add_request where receiver_id = ? and req_status = 1";
        List<AddRequest> list = jdbcTemplate.query(sql, new Object[]{userId},
                new RowMapper<AddRequest>() {
                    @Override
                    public AddRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AddRequest addRequest = new AddRequest();
                        addRequest.setReceiverId(rs.getString("receiver_id"));
                        addRequest.setSenderId(rs.getString("sender_id"));
                        return addRequest;
                    }
                }
            );
        return list;
    }

    public int deniedRequest(AddRequest addRequest){
        String sql = "delete from add_request where receiver_id = ? and sender_id = ?";
        return jdbcTemplate.update(sql, addRequest.getReceiverId(), addRequest.getSenderId());
    }

    public int acceptRequest(AddRequest addRequest){
        String sql = "delete from add_request where receiver_id = ? and sender_id = ?";
        jdbcTemplate.update(sql, addRequest.getReceiverId(), addRequest.getSenderId());

        sql = "insert into friend_list(user_id,friend_id) values (?,?)";
        return jdbcTemplate.update(sql,addRequest.getReceiverId(), addRequest.getSenderId());
    }

    public int blockUser(AddRequest addRequest){
        String sql = "update add_request set req_status = 0 where receiver = ? and sender_id = ?";
        return jdbcTemplate.update(sql, addRequest.getReceiverId(), addRequest.getSenderId());
    }
}

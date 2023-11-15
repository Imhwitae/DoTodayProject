package com.todolist.DoToday.repository;

import com.todolist.DoToday.dto.request.MemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapperRepository {

//    @Select("select * from member where member_id = #{id}")
//    public MemberDetailDto findById(String id);
}

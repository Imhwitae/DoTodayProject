package com.todolist.DoToday.mapper;

import com.todolist.DoToday.response.MemberDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapperRepository {
    @Select("select * from member")
    List<MemberDetailDto> findAll();

    @Select("select * from member where member_id = #{member_id}")
    MemberDetailDto findById(@Param("member_id") String member_id);

    @Select("select member_num from member where member_id = #{member_id}")
    Long findMemberNum (@Param("member_id") String id);
}

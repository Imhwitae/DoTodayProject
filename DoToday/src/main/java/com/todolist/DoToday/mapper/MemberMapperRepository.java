package com.todolist.DoToday.mapper;

import com.todolist.DoToday.dto.response.MemberDetailDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapperRepository {
    @Select("select * from member")
    List<MemberDetailDto> findAll();

    @Select("select * from member where member_id = #{memberId}")
    MemberDetailDto findById(@Param("memberId") String id);

    @Select("select member_num from member where member_id = #{member_id}")
    Long findMemberNum (@Param("member_id") String id);
}

package com.todolist.DoToday.mapper;

import com.todolist.DoToday.dto.request.SocialMemberJoinDto;
import com.todolist.DoToday.dto.response.MemberDetailDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MemberMapper {
    @Select("select * from member")
    List<MemberDetailDto> findAll();

    @Select("select * from member where member_id = #{memberId}")
    MemberDetailDto findById(@Param("memberId") String id);

    @Select("select member_num from member where member_id = #{memberId}")
    Long findMemberNum (@Param("memberId") String id);

    @Insert("insert into member (member_id, member_pw, member_name, member_image, member_birth," +
            "member_regdate, member_gender, member_enabled)" +
            "values (#{memberId}, #{memberPw}, #{memberName}, #{memberImage}, #{memberBirth}, #{memberRegdate}," +
            "#{memberGender}, #{memberEnabled})")
    void insertMember(@Param("memberId") String id,
                      @Param("memberPw") String pw,
                      @Param("memberName") String name,
                      @Param("memberImage") String image,
                      @Param("memberBirth") LocalDate birth,
                      @Param("memberRegdate") LocalDate regdate,
                      @Param("memberGender") String gender,
                      @Param("memberEnabled") boolean enabled);

    @Insert("insert into member (member_id, member_pw, member_name, member_image," +
            "member_regdate, member_enabled)" +
            "values (#{memberId}, #{memberPw}, #{memberName}, #{memberImage}, #{memberRegdate}, #{memberEnabled})")
    void ApiInsertMember(@Param("memberId") String id,
                         @Param("memberPw") String pw,
                         @Param("memberName") String name,
                         @Param("memberImage") String image,
                         @Param("memberRegdate") LocalDate regdate,
                         @Param("memberEnabled") boolean enabled);

    @Insert("insert into member (member_birth, member_gender) values (#{memberBirth}, #{memberGender})")
    void insertMemberEtcInfo(@Param("memberBirth") LocalDate birth, @Param("memberGender") String gender);

    @Insert("insert into member(member_id, member_pw, member_name, member_image, member_gender, " +
            "member_regdate, member_enabled)" +
            "values (#{socialMember.memberId}, #{socialMember.memberPw}, #{socialMember.memberName}, " +
            "#{socialMember.memberImage}, #{socialMember.memberGender}, #{socialMember.memberRegdate}, #{socialMember.memberEnabled})")
    void joinSocialMember(@Param("socialMember") SocialMemberJoinDto socialMember);

    @Update("update member set member_image = #{memberImage} where member_id = #{memberId}")
    void updateImage(@Param("memberImage") String img, @Param("memberId") String memberId);

    @Update("update member set member_pw = #{memberPw} where member_id = #{memberId}")
    void updatePw(@Param("memberPw") String pw, @Param("memberId") String id);

    @Update("update member set member_name = #{socialMember.memberName}, member_image = #{socialMember.memberImage} " +
            "where member_id = #{socialMember.memberId};")
    void updateImgId(@Param("socialMember") SocialMemberJoinDto socialMember);
}

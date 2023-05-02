package com.sh.oee.together.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import com.sh.oee.member.model.dto.Member;
import com.sh.oee.together.model.dto.JoinMember;
import com.sh.oee.together.model.dto.Together;
import com.sh.oee.together.model.dto.TogetherEntity;

@Mapper
public interface TogetherDao {

	List<Together> selectTogetherList(Member member);
	
	List<TogetherEntity> selectTogetherList(String memberId);
	
	@Select("select * from together_category")
	List<Map<String,String>> selectTogetherCategory();

	List<Together> selectTogetherListByDongName(Map<String, Object> param, RowBounds rowBounds);

	Together selectTogetherByNo(int no);

	int insertTogether(TogetherEntity together);

	int insertTogetherChat(Map<String, Object> param);

	@Delete("delete together where no = #{no}")
	int togetherDelete(int no);

	int togetherUpdate(TogetherEntity together);

	@Update("update together set status = 'N' where no = #{no}")
	int togetherStatusUpdate(int no);

	int getTogetherTotalCount(Map<String, Object> param);

	List<JoinMember> joinMemberListByBoardNo(Map<String, Object> params);

	List<Map<String, Object>> getJoinMemberCnt(Map<String, Object> param);
	
	@Select("select count(*) from together_chat where together_no = #{no}")
	int getCurrentJoinCnt(int no);

	void TimeOverDateTimeUpdate();

	List<Together> selectTogether1List(String memberId);

	@Select("select * from together where no = #{no}")
	Together findTogetherByChatroomNo(int no);
	
	

}

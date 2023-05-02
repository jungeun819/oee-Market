package com.sh.oee.together.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.sh.oee.member.model.dto.Member;
import com.sh.oee.together.model.dto.JoinMember;
import com.sh.oee.together.model.dto.Together;
import com.sh.oee.together.model.dto.TogetherEntity;

public interface TogetherService {

	List<Together> selectTogetherList(Member member);

	List<Map<String,String>> selectTogetherCategory();

//	List<Together> selectTogetherListByDongName(List<String> myDongList, RowBounds rowBounds);
	List<Together> selectTogetherListByDongName(Map<String, Object> param, RowBounds rowBounds);

	Together selectTogetherByNo(int no);

	int insertTogether(TogetherEntity together);

	int togetherDelete(int no);

	int togetherUpdate(TogetherEntity together);

	int togetherStatusUpdate(int no);

	int getTogetherTotalCount(Map<String, Object> param);

	List<JoinMember> joinMemberListByBoardNo(Map<String, Object> params);

	List<Map<String, Object>> getJoinMemberCnt(Map<String, Object> param);

	void TimeOverDateTimeUpdate();

	List<Together> selectTogether1List(String memberId);

	/* 효정 시작 */
	Together findTogetherByChatroomNo(int no);
	/* 효정 끝 */
	
	int getCurrentJoinCnt(int no);
}

package com.sh.oee.together.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sh.oee.member.model.dto.Member;
import com.sh.oee.together.model.dao.TogetherDao;
import com.sh.oee.together.model.dto.JoinMember;
import com.sh.oee.together.model.dto.Together;
import com.sh.oee.together.model.dto.TogetherEntity;

@Service
@Transactional(rollbackFor = Exception.class)
public class TogetherServiceImpl implements TogetherService {

	@Autowired
	private TogetherDao togetherDao;

	@Override
	public List<Together> selectTogetherList(Member member) {
		return togetherDao.selectTogetherList(member);
	}
	
	@Override
	public List<Map<String,String>> selectTogetherCategory() {
		return togetherDao.selectTogetherCategory();
	}

	@Override
	public List<Together> selectTogetherListByDongName(Map<String, Object> param, RowBounds rowBounds) {
		return togetherDao.selectTogetherListByDongName(param, rowBounds);
	}

	@Override
	public Together selectTogetherByNo(int no) {
		return togetherDao.selectTogetherByNo(no);
	}

	@Override
	public int insertTogether(TogetherEntity together) {
		// 같이해요 게시글 등록
		int result = togetherDao.insertTogether(together);
		// 같이해요 채팅방 생성..?
		Map<String, Object> param = new HashMap<>();
		param.put("togetherNo", together.getNo());
		param.put("memberId", together.getWriter());
		param.put("role", 'A');
		insertTogetherChat(param);
		
		return result;
	}
	
	private int insertTogetherChat(Map<String, Object> param) {
		return togetherDao.insertTogetherChat(param);
	}

	@Override
	public int togetherDelete(int no) {
		return togetherDao.togetherDelete(no);
	}

	@Override
	public int togetherUpdate(TogetherEntity together) {
		return togetherDao.togetherUpdate(together);
	}

	@Override
	public int togetherStatusUpdate(int no) {
		return togetherDao.togetherStatusUpdate(no);
	}

	@Override
	public int getTogetherTotalCount(Map<String, Object> param) {
		return togetherDao.getTogetherTotalCount(param);
	}

	@Override
	public List<JoinMember> joinMemberListByBoardNo(Map<String, Object> params) {
		return togetherDao.joinMemberListByBoardNo(params);
	}

	@Override
	public List<Map<String, Object>> getJoinMemberCnt(Map<String, Object> param) {
		return togetherDao.getJoinMemberCnt(param);
	}
	
	@Override
	public int getCurrentJoinCnt(int no) {
		return togetherDao.getCurrentJoinCnt(no);
	}

	@Override
	public void TimeOverDateTimeUpdate() {
		togetherDao.TimeOverDateTimeUpdate();
	}

	@Override
	public List<Together> selectTogether1List(String memberId) {
		return togetherDao.selectTogether1List(memberId);
	}
	
	/* 효정 시작 */
	@Override
	public Together findTogetherByChatroomNo(int no) {
		return togetherDao.findTogetherByChatroomNo(no);
	}
	/* 효정 끝 */
}

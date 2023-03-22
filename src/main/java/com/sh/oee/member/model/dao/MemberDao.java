package com.sh.oee.member.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.sh.oee.member.model.dto.Dong;
import com.sh.oee.member.model.dto.Gu;
import com.sh.oee.member.model.dto.DongRange;
import com.sh.oee.member.model.dto.Member;

@Mapper
public interface MemberDao {

	/** 정은 시작 */
	Member selectOneMember(String memberId);

	@Select("select * from gu")
	List<Gu> selectGuList();
	
	@Select("select * from dong")
	List<Dong> selectDongList();


	/** 정은 끝 */
	
	/**
	 * 하나 시작
	 */
	int updateMember(Member member);
	@Update("UPDATE member SET delete_date = sysdate WHERE member_id = #{ memberId }")
	int memberDelete(Member member);
	/**
	 * 하나 끝
	 */

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

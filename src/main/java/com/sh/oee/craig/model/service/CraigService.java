package com.sh.oee.craig.model.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.http.ResponseEntity;

import com.sh.oee.craig.model.dto.Craig;
import com.sh.oee.craig.model.dto.CraigAttachment;
import com.sh.oee.craig.model.dto.CraigEntity;

public interface CraigService {

	// 전체목록조회 
	List<Craig> craigList(RowBounds rowBounds,  List<String> dongList );

	// 카테고리 목록 조회 
	List<Map<String, String>> craigCategoryList();

	// 게시글등록
	int insertCraigBoard(Craig craig);
	
	//첨부파일등록
	int insertCraigAttachment(CraigAttachment attach);

	//게시글+첨부파일 함께 조회 
	Craig selectcraigOne(int no);

	//내카테
	String selectMyCraigCategory(int categoryNo);

	//update -사실은 insert도같이 
	int updateCraigBoard(Craig craig);

	
	//delete 원래 첨부파일
	int deleteCraigAttachment(int orifileattno);

	//첨부파일 조회 
	List<CraigAttachment> selectcraigAttachments(int no);


}

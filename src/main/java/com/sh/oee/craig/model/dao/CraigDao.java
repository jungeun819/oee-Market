package com.sh.oee.craig.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.sh.oee.craig.model.dto.CraigEntity;

@Mapper
public interface CraigDao {

	//전체목록조회
	List<CraigEntity> craigList();

	@Select("select * from craig_category")
	List<Map<String, String>> craigCategoryList();

}

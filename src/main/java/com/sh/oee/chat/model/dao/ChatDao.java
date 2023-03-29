package com.sh.oee.chat.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sh.oee.chat.model.dto.CraigChat;
import com.sh.oee.chat.model.dto.CraigMsg;

@Mapper
public interface ChatDao {

	@Select("select chatroom_id from craig_chat where member_id = #{memberId} and craig_no = #{craigNo}")
	String findCraigChatroomId(Map<String, Object> craigChatMap);

	@Insert("insert into craig_chat (chatroom_id, member_id, craig_no) values(#{chatroomId}, #{memberId}, #{craigNo})")
	int insertCraigChatroom(CraigChat craigChat);

	@Select("select * from craig_msg where chatroom_id = #{chatroomId} order by msg_no")
	List<CraigMsg> findCraigMsgBychatroomId(String chatroomId);

	@Select("select member_id from craig_chat where chatroom_id = #{chatroomId} and member_id != #{memberId}")
	String findOtherFromCraigChat(Map<String, Object> startUser);

	@Select("select chatroom_id from craig_chat where member_id = #{memberId} and craig_no = #{craigNo}")
	List<String> findCraigChatList(Map<String, Object> craigChatMap);

	@Insert("insert into craig_msg values (seq_craig_msg_no.nextval, #{chatroomId}, #{writer}, #{content}, #{sentTime}, #{type})")
	int insertCraigMsg(CraigMsg craigMsg);

	@Select("select first_value(content) over (order by sent_time) from craig_msg where chatroom_id = #{chatroomId}")
	String findLastChatByChatroomId(String chatroomId);



}

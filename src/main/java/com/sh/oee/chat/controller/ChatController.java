package com.sh.oee.chat.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sh.oee.chat.model.dto.CraigChat;
import com.sh.oee.chat.model.dto.CraigMsg;
import com.sh.oee.chat.model.service.ChatService;
import com.sh.oee.craig.model.dto.Craig;
import com.sh.oee.craig.model.service.CraigService;
import com.sh.oee.member.model.dto.Member;
import com.sh.oee.member.model.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;
	@Autowired
	private CraigService craigService;
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/chatList.do")
	public void chatList(Model model) {
		// 중고거래 채팅목록 조회

	}
	
	/**
	 * 중고거래 게시글에서 채팅하기 선택시
	 */
	@ResponseBody
	@GetMapping("/craigChat/{craigNo}")
	public Map<String, Object> craigChat(@PathVariable int craigNo, Authentication authentication, Model model, HttpSession session){
		
		// 1. 로그인한 사용자 id 꺼내기
		String memberId = ((Member) authentication.getPrincipal()).getMemberId();
		log.debug("구매자 = {}", memberId);
		
		// 2. 게시글 번호로 게시글객체 -> 판매자정보 꺼내기
//		Craig craig = craigService.selectcraigOne(craigNo);
//		String sellerId = craig.getWriter();
		
//----------------- 혜진 추가	-----------------------------------------			
		boolean hasRead = true; // 원래 썼던 코드는 주석해두고, 혜진추가 0326
		Craig craig = craigService.selectcraigOne(craigNo, hasRead);
		String sellerId = craig.getWriter();
//---------------------------------------------------------------------	
		
		log.debug("판매자 = {}", sellerId);
		

		// 3. memberId, craigNo로 chatroom_id 조회
		// parameter는 1개만 가능하므로 Map에 담는다
		Map<String, Object> craigChatMap = new HashMap<>();
		craigChatMap.put("memberId", memberId);
		craigChatMap.put("craigNo", craigNo);
		
		String chatroomId = chatService.findCraigChatroomId(craigChatMap);

		// 채팅방 첫 입장시
		if(chatroomId == null) {
			// 1. chatroomId 생성
			chatroomId = generateCraigChatroomId();
			log.debug("채팅방번호 = {}", chatroomId);
			
			// 2. craig_chat 테이블에 2행 insert (로그인한 사용자memberId, 게시글 작성자 sellerId)
			List<CraigChat> chatMembers = Arrays.asList(
					new CraigChat(chatroomId, memberId, craigNo),
					new CraigChat(chatroomId, sellerId, craigNo));
			int result = chatService.createCraigChatroom(chatMembers);
		}

		Map<String, Object> map = new HashMap<>();
		map.put("memberId", memberId);
		map.put("chatroomId", chatroomId);
		
		return map;
	}
	
	/**
	 * 중고거래 채팅방 chatroomId 생성
	 */
	private String generateCraigChatroomId() {
		StringBuilder chatroomId = new StringBuilder();
		Random random = new Random();
		final int LEN = 12;
		// 반복처리
		for(int i = 0; i < LEN; i++) {
			// 숫자
			if(random.nextBoolean()) {
				chatroomId.append(random.nextInt(10));
			}
			// 영문자
			else {
				// 대문자
				if(random.nextBoolean()) {
					chatroomId.append((char) (random.nextInt(26) + 'A'));
				}
				// 소문자
				else {
					chatroomId.append((char) (random.nextInt(26) + 'a'));
				}
			}
		}
		return chatroomId.toString();
	}
	
	/**
	 * 중고거래 채팅방 팝업 열기
	 */
	@GetMapping("/craigChat.do")
	public String craigChatPop(@RequestParam String chatroomId, @RequestParam String memberId, @RequestParam int craigNo, Model model) {
		// 1. 채팅방을 연 사람이 누구인지 확인
//		log.debug("연사람확인 = {}", memberId);
		model.addAttribute("memberId", memberId);
		
		// 2. 전달받은 chatroomId로 대화내역 찾기
		List<CraigMsg> craigMsg = chatService.findCraigMsgBychatroomId(chatroomId);
		model.addAttribute("craigMsg", craigMsg);
		
		// 3. 대화상대 정보 찾기
		// 채팅방 아이디로 채팅방을 조회하고, 나온 멤버중 본인을 제외한 멤버 가져옴
		Map<String, Object> startUser = new HashMap<>();
		startUser.put("memberId", memberId);
		startUser.put("chatroomId", chatroomId);
		String otherUserId = chatService.findOtherFromCraigChat(startUser);
//		log.debug("대화상대 = {}", otherUserId);
		// 조회한 아이디로 Member객체 조회
		Member otherUser = memberService.selectOneMember(otherUserId);
//		log.debug("otherUser = {}", otherUser);
		model.addAttribute("otherUser", otherUser);

		// 4. 해당게시글 찾기 
//		Craig craig = craigService.selectcraigOne(craigNo); 

//----------------- 혜진 추가	-----------------------------------------	
		boolean hasRead = true; //혜진추가 0326
		Craig craig = craigService.selectcraigOne(craigNo, hasRead); 
//------------------------------------------------------------------	
		
		log.debug("craig = {}", craig);
		model.addAttribute("craig", craig);
		
		return "chat/craigChatroom";
	}
}

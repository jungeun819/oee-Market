package com.sh.oee.together.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sh.oee.common.OeeUtils;
import com.sh.oee.member.model.dto.Member;
import com.sh.oee.together.model.dto.JoinMember;
import com.sh.oee.together.model.dto.Together;
import com.sh.oee.together.model.dto.TogetherEntity;
import com.sh.oee.together.model.service.TogetherService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/together")
@Slf4j
public class TogetherController {
	
	@Autowired
	private TogetherService togetherService;
	
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	/** 🐱 하나 시작 🐱 */
	
	@GetMapping("/myTogether.do")
	public void together(Authentication authentication, Model model) {
		// member  
		Member member = ((Member)authentication.getPrincipal());
		log.debug("member = {} ", member);
		
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory();
		List<Together> myTogether = togetherService.selectTogetherList(member);
		
		log.debug("myTogether = {}",myTogether);
		
		model.addAttribute("categorys", categorys);
		model.addAttribute("myTogether",myTogether);
	}
	@GetMapping("/myTogether1.do")
	public void together(@RequestParam String memberId, Model model) {
		// member  
		log.debug("memberId = {} ", memberId);
		
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory();
		List<Together> myTogether = togetherService.selectTogether1List(memberId);
		
		log.debug("myTogether = {}",myTogether);
		
		model.addAttribute("categorys", categorys);
		model.addAttribute("myTogether",myTogether);
	}
		
	/** 🐱 하나 끝 🐱 */
	
	
	/** 👻 정은 시작 👻 */
	
	/**
	 * 같이해요 목록조회
	 * @param session
	 * @param model
	 */
	@GetMapping("/togetherList.do")
	public void togetherList(@RequestParam(defaultValue = "1") int currentPage, 
							 @RequestParam(required = false) String categoryNo,
							 @RequestParam(required = false) String status,
							 HttpSession session, 
							 Model model) {
		log.debug("categoryNo = {}", categoryNo);
		log.debug("status = {}", status);
		
		Integer no = null;
		try {
			no = Integer.parseInt(categoryNo);
		} catch (NumberFormatException e) {}
		
		// 나의 동네범위 꺼내기
		List<String> myDongList = (List<String>)session.getAttribute("myDongList");
		log.debug("myDongList ={}", myDongList);

		// map 
		Map<String, Object> param = new HashMap<>();
		param.put("myDongList", myDongList);
		param.put("categoryNo", no);
		param.put("status", status);

		// 페이지처리
		int limit = 6;
		int offset = (currentPage - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		int totalCount = togetherService.getTogetherTotalCount(param);
		
		// 전체 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / rowBounds.getLimit());
		
		// 업무로직
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory(); // 카테고리 목록
		List<Together> togetherList = togetherService.selectTogetherListByDongName(param, rowBounds); // 페이징처리된 게시물목록
		
		// 위에서 가져온 같이해요 목록의 번호 추출
		List<Integer> boardNoList = new ArrayList<>();
		for(int i = 0; i < togetherList.size(); i++) {
			boardNoList.add(togetherList.get(i).getNo());
		}
		Map<String, Object> params = new HashMap<>(); // 왜 때문에 list는 안되고 map만 매개변수에 담길까,,?
		params.put("boardNoList", boardNoList);
	
		if(boardNoList.size() > 0) {
			List<JoinMember> joinMemberList = togetherService.joinMemberListByBoardNo(params); // 현재 참여하는 이웃 목록
			List<Map<String, Object>> joinCntList = togetherService.getJoinMemberCnt(params);
			model.addAttribute("joinMemberList", joinMemberList);
			model.addAttribute("joinCntList", joinCntList);
		}
		
		// view 전달
		model.addAttribute("categorys", categorys);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("togetherList", togetherList);
	}
	
	/**
	 * 같이해요 상세조회
	 * @param no
	 * @param model
	 */
	@GetMapping("/togetherDetail.do")
	public void togetherDetail(@RequestParam(required = false) String category, @RequestParam int no, Model model) {
		log.debug("no = {}", no);
		
		List<Integer> boardNoList = new ArrayList<>();
		boardNoList.add(no);
		Map<String, Object> params = new HashMap<>(); // 왜 때문에 list는 안되고 map만 매개변수에 담길까,,?
		params.put("boardNoList", boardNoList);
		
		// 업무로직
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory();
		Together together = togetherService.selectTogetherByNo(no);
		List<JoinMember> joinMemberList = togetherService.joinMemberListByBoardNo(params); // 현재 참여하는 이웃 목록
		List<Map<String, Object>> joinCnt = togetherService.getJoinMemberCnt(params);
		
		// 개행, 자바스크립트 코드 방어
		together.setContent(OeeUtils.convertLineFeedToBr(OeeUtils.escapeHtml(together.getContent())));
		
		// view 전달
		model.addAttribute("categorys", categorys);
		model.addAttribute("together", together);
		model.addAttribute("joinMemberList", joinMemberList);
		model.addAttribute("joinCnt", joinCnt);
	}
	
	/**
	 * 같이해요 폼 불러오기
	 * @param model
	 */
	@GetMapping("/togetherEnroll.do")
	public void togetherEnroll(Model model) {	
		// 업무로직
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory();
		
		// view 전달
		model.addAttribute("categorys", categorys);
		model.addAttribute("today", new Date());
	}
	
	/**
	 * 같이해요 등록
	 * @param together
	 * @param month
	 * @param date
	 * @param meridiem
	 * @param hour
	 * @param minute
	 * @return
	 */
	@PostMapping("/togetherEnroll.do")
	public String togetherEnroll(TogetherEntity together, 
								@RequestParam String month,
								@RequestParam String date,
								@RequestParam String meridiem,
								@RequestParam String hour,
								@RequestParam String minute,
								RedirectAttributes redirectAttr) {
		
		// LocalDateTime 객체 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss");
		LocalDateTime dateTime = LocalDateTime.now() // 현재 시각 (사용할 경우)
							.withMonth(Integer.parseInt(month))
							.withDayOfMonth(Integer.parseInt(date))
							.withHour(meridiem.equals("pm") && Integer.parseInt(hour) != 12 ? 
									Integer.parseInt(hour) + 12 : meridiem.equals("am") && Integer.parseInt(hour) != 12 ?
											Integer.parseInt(hour) : meridiem.equals("pm") && Integer.parseInt(hour) == 12 ?
													12 : 0
									)
		                    .withMinute(Integer.parseInt(minute))
		                    .withSecond(0)
		                    .withNano(0);
		
		log.debug("dateTime = {}", dateTime);
		
		String formattedDateTime = dateTime.format(formatter); // 포맷팅된 날짜시간 문자열
		log.debug("formattedDateTime = {}", formattedDateTime);

		together.setDateTime(dateTime);
		
		// 업무로직
		int result = togetherService.insertTogether(together);
		
		// view 전달
		redirectAttr.addFlashAttribute("msg", "게시글을 등록했습니다!");
		
		return "redirect:/together/togetherList.do";
	}

	/**
	 * 같이해요 수정 폼 불러오기
	 * @param no
	 */
	@GetMapping("/togetherUpdate.do")
	public void togetherUpdate(@RequestParam int no, Model model) {
		log.debug("no ={}", no);
		
		List<Integer> boardNoList = new ArrayList<>();
		boardNoList.add(no);
		Map<String, Object> params = new HashMap<>(); // 왜 때문에 list는 안되고 map만 매개변수에 담길까,,?
		params.put("boardNoList", boardNoList);
		
		
		// 업무로직
		Together together = togetherService.selectTogetherByNo(no);
		List<Map<String,String>> categorys = togetherService.selectTogetherCategory();
		log.debug("together = {}", together);
		List<Map<String, Object>> joinCnt = togetherService.getJoinMemberCnt(params);
		
		
		// view 전달
		model.addAttribute("together", together);
		model.addAttribute("categorys", categorys);
		model.addAttribute("today", new Date());
		model.addAttribute("joinCnt", joinCnt);
		
	}
	
	/**
	 * 같이해요 수정
	 * @param together
	 * @return
	 */
	@PostMapping("/togetherUpdate.do")
	public String togetherUpdate(TogetherEntity together, 
								@RequestParam String month,
								@RequestParam String date,
								@RequestParam String meridiem,
								@RequestParam String hour,
								@RequestParam String minute) {
		log.debug("together = {}", together);
		log.debug(hour);
		
		// LocalDateTime 객체 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd a hh:mm:ss");
		LocalDateTime dateTime = LocalDateTime.now() // 현재 시각 (사용할 경우)
							.withMonth(Integer.parseInt(month))
							.withDayOfMonth(Integer.parseInt(date))
							.withHour(meridiem.equals("pm") && Integer.parseInt(hour) != 12 ? 
												Integer.parseInt(hour) + 12 : meridiem.equals("am") && Integer.parseInt(hour) != 12 ?
														Integer.parseInt(hour) : meridiem.equals("pm") && Integer.parseInt(hour) == 12 ?
																12 : 0
									 )
		                    .withMinute(Integer.parseInt(minute))
		                    .withSecond(0)
		                    .withNano(0);
		
		log.debug("dateTime = {}", dateTime);
		
		String formattedDateTime = dateTime.format(formatter); // 포맷팅된 날짜시간 문자열
		log.debug("formattedDateTime = {}", formattedDateTime);

		together.setDateTime(dateTime);
		
		// 업무로직
		int result = togetherService.togetherUpdate(together);
		
		return "redirect:/together/togetherDetail.do?no=" + together.getNo();
	}
	
	/**
	 * 같이해요 삭제
	 * @param no
	 * @param redirectAttr
	 * @return
	 */
	@PostMapping("/togetherDelete.do")
	public String togetherDelete(@RequestParam int no, RedirectAttributes redirectAttr) {
		log.debug("no = {}", no);
		
		// 업무로직
		int result = togetherService.togetherDelete(no);
		
		// view 전달
		redirectAttr.addFlashAttribute("msg", "게시글을 삭제되었습니다!");
		
		return "redirect:/together/togetherList.do";
	}
	
	/**
	 * 같이해요 모임종료
	 * @param no
	 * @param redirectAttr
	 * @return
	 */
	@PostMapping("/togetherStatusUpdate.do")
	public String togetherStatusUpdate(@RequestParam int no, RedirectAttributes redirectAttr) {
		log.debug("no = {}", no);
		
		// 업무로직
		int result = togetherService.togetherStatusUpdate(no);
		
		// view 전달
		redirectAttr.addFlashAttribute("msg", "모임이 종료되었습니다!😊");
		
		return "redirect:/together/togetherDetail.do?no=" + no;
	}
	
	/**
	 * 현재 참여중인 이웃수 확인
	 * @param no
	 * @return
	 */
	@ResponseBody
	@GetMapping("/currentJoinCnt.do")
	public int currentJoinCnt(@RequestParam int no) {
		log.debug("no = {}", no);
		
		// 업무로직
		int currentJoinCnt = togetherService.getCurrentJoinCnt(no);
		log.debug("currentJoinCnt = {}", currentJoinCnt);
		
		return currentJoinCnt;
	}
	
	/** 👻 정은 끝 👻 */
	
}

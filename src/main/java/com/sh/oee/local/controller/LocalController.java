package com.sh.oee.local.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sh.oee.common.OeeUtils;
import com.sh.oee.local.model.dto.Local;

import com.sh.oee.local.model.dto.LocalAttachment;
import com.sh.oee.local.model.dto.LocalCategory;
import com.sh.oee.local.model.dto.LocalCommentEntity;
import com.sh.oee.local.model.dto.LocalEntity;
import com.sh.oee.local.model.dto.LocalLike;
import com.sh.oee.local.model.service.LocalService;
import com.sh.oee.member.model.dto.Dong;
import com.sh.oee.member.model.dto.Member;

import com.sh.oee.member.model.service.MemberService;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

import com.sh.oee.together.model.dto.Together;


import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j 
@RequestMapping("/local")
public class LocalController {
	
	@Autowired
	private LocalService localService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ServletContext application;
	
	//동네생활 게시물 목록
	@GetMapping("/localList.do")
	public void localList(Model model , HttpSession session, 
			@RequestParam(defaultValue = "1") int currentPage, 
			 @RequestParam(required = false) String categoryNo) {
	
		log.debug("categoryNo = {}", categoryNo);

		Integer no = null;
		try {
			no = Integer.parseInt(categoryNo);
		} catch (NumberFormatException e) {}
		
		//글 목록
		List<String> myDongList = (List<String>)session.getAttribute("myDongList");
		log.debug("dongList = {}",myDongList);
		
		Map<String, Object> param = new HashMap<>();
		param.put("myDongList", myDongList);
		param.put("categoryNo", no);
		
		int limit = 10;
		int offset = (currentPage - 1) * limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		int totalCount = localService.getLocalTotalCount(param);
		
		List<Map<String,String>> localCategory = localService.localCategoryList();
		List<Local> localList = localService.selectLocalListByDongName(param, rowBounds);
		
		
		int totalPages = (int) Math.ceil((double) totalCount / rowBounds.getLimit());
		
		
		// 위에서 가져온 같이해요 목록의 번호 추출
				List<Integer> boardNoList = new ArrayList<>();
				for(int i = 0; i < localList.size(); i++) {
					boardNoList.add(localList.get(i).getNo());
				}
				Map<String, Object> params = new HashMap<>(); 
				params.put("boardNoList", boardNoList);
		
		//동네정보 가져오기
		log.debug("localList = {}", localList);
		log.debug("localCategory = {} ", localCategory);
		
		//아이디 가져오기
//		Member member =((Member)authentication.getPrincipal());
//		log.debug("writeMemebr = {}", member);
//		
		
		
		//view단
		model.addAttribute("localList", localList);
		model.addAttribute("localCategory",localCategory);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
//		model.addAttribute("member",member);
	}
	
	
	
	// 동네생활 글쓰기 페이지
	@GetMapping("/localEnroll.do")
	public void localEnroll(Model model) {
		
		List<Map<String,String>> localCategory = localService.localCategoryList();
		
		log.debug("localCategroy = {}", localCategory);
		
		model.addAttribute("localCategory",localCategory);
	}
	
	//동네생활 글 등록
	@PostMapping("/localBoardEnroll.do")
	public String insertLocalBoard(Local local,
			@RequestParam("upFile") List<MultipartFile> upFiles,
			RedirectAttributes redirectAttr) {
		
		String saveDirectory = application.getRealPath("/resources/upload/local");
		log.debug("saveDirectory = {}",saveDirectory);
		
	//첨부파일 저장 및 Attachment 객체 만들기
		for(MultipartFile upFile : upFiles) {
			log.debug("upFile = {} ", upFile);
			log.debug("upFile = {} ", upFile.getOriginalFilename());
			log.debug("upFileSize = {} ", upFile.getSize());
			
			if(upFile.getSize() > 0) {
				// 저장
				String renamedFilename = OeeUtils.renameMultipartFile(upFile);
				String originalFilename = upFile.getOriginalFilename();
				File destFile = new File(saveDirectory, renamedFilename);
				try {
					upFile.transferTo(destFile);
				}  catch (IllegalStateException | IOException e) {
					log.error(e.getMessage(), e);
				}
				
				// attach객체 생성 및 Board에 추가
				LocalAttachment attach = new LocalAttachment();
				attach.setReFilename(renamedFilename);
				attach.setOriginalFilename(originalFilename);
				local.addAttachment(attach);
			}
		}
		
		//Board 저장
		int result = localService.insertLocalBoard(local);
		log.debug("result : " + result);
		
		redirectAttr.addFlashAttribute("msg", "동네생활 게시글을 등록했습니다.");
		
		return "redirect:/local/localList.do";
	}
	
	
	//한건조회(상세페이지)
	@GetMapping("/localDetail.do")
	public void localDetail(LocalCategory localcategory,@RequestParam int no, Model model,Authentication authentication) {
		Local localdetail = localService.selectLocalOne(no);
		Member member = ((Member)authentication.getPrincipal());
		
		Map<String, Object> param = new HashMap<>();
		 param.put("memberId", member.getMemberId());
		 param.put("no", no);
		
		localdetail.setContent(OeeUtils.convertLineFeedToBr(OeeUtils.escapeHtml(localdetail.getContent())));
		
		log.debug("localdetail : {}", localdetail);
		
		//조회수 증가
		int readCnt = localService.hits(no);
		
		int findlike = localService.selectLocalLike(param);
		
		List<LocalCommentEntity> commentList = localService.selectLocalCommentListByBoardNo(no);
		
		model.addAttribute("localcategory", localcategory);
		model.addAttribute("localdetail", localdetail);
		model.addAttribute("commentList", commentList);
		model.addAttribute("findlike", findlike);
	}
	
	//좋아요
	@ResponseBody
	@PostMapping("/localLike.do")
	public int localLike(@RequestParam int no, String memberId) {
		Map<String,Object> param = new HashMap<>();
		
		param.put("memberId", memberId);
		param.put("no", no);
		
		int locallike = localService.selectLocalLike(param);
		
		int result = 0;
		if(locallike == 1) {
			result = localService.DeleteLocalLike(param);
			result = 0;
		} else {
			result = localService.InsertLocalLike(param);
		}
		log.debug("like result : {}" + result);
		
		return result;
	}
	
	// 글 수정하기 폼 이동
	@GetMapping("/localUpdate.do")
	public void localUpdate(@RequestParam int no,Model model) {
		log.debug("no={}",no);
		
		Local localdetail = localService.selectLocalOne(no);
		List<Map<String,String>> localCategory = localService.localCategoryList();
		List<LocalAttachment> originalFiles = localService.selectLocalAttachments(no);
		log.debug("localdetail ={}",localdetail);
		
		model.addAttribute("localdetail",localdetail);
		model.addAttribute("localCategory", localCategory);
		model.addAttribute("originalFiles", originalFiles);
		
	}
	
	// 글 수정하기
	@PostMapping("/localUpdate.do")
	public String localUpdate(Local local,
			@RequestParam("upFile") List<MultipartFile> upFiles) {
		
		String saveDirectory = application.getRealPath("/resources/upload/local");
			
		
		//기존 첨부파일번호 조회
		if(upFiles != null || upFiles.size() > 0) {
			int attachNo = localService.selectAttachNo(local.getNo());	
			
			LocalAttachment attach = new LocalAttachment();
			//첨부파일 저장 및 Attachment 객체 만들기
			for(MultipartFile upFile : upFiles) {
				log.debug("upFile = {} ", upFile);
				log.debug("upFile = {} ", upFile.getOriginalFilename());
				log.debug("upFileSize = {} ", upFile.getSize());
				
				if(upFile.getSize() > 0) {
					// 저장
					String renamedFilename = OeeUtils.renameMultipartFile(upFile);
					String originalFilename = upFile.getOriginalFilename();
					File destFile = new File(saveDirectory, renamedFilename);
					try {
						upFile.transferTo(destFile);
					}  catch (IllegalStateException | IOException e) {
						log.error(e.getMessage(), e);
					}
					
					// attach객체 생성 및 Board에 추가
					attach.setReFilename(renamedFilename);
					attach.setOriginalFilename(originalFilename);
					attach.setAttachNo(attachNo);
					//local.addAttachment(attach);
				}
			}
			localService.updateAttachFile(attach);
		}
		
		
				
		//Board 저장
		int result = localService.updateLocalBoard(local);
		log.debug("result : " + result);

		return "redirect:/local/localDetail.do?no=" + local.getNo();
	}
	
	
	// 글 삭제하기
	@PostMapping("/localDelete.do")
	public String localDelete(@RequestParam int no, RedirectAttributes redirectAttr) {
		log.debug("no={}", no);
		
		int result = localService.deleteLocal(no);
		
		redirectAttr.addFlashAttribute("msg","게시글을 삭제했습니다.");
		
		return "redirect:/local/localList.do";
	}
	
	

	
	//댓글 입력
	@RequestMapping(value = "/commentInsert.do", method = RequestMethod.POST)
	public String commentInsert(LocalCommentEntity comment, Authentication authentication, RedirectAttributes redirectAttr) {
		//public void commentInsert(@RequestParam Map<String, Object> param, HttpSession session) {
		log.debug("commentㅁㄴ아러밎러 : " + comment );
		//Member member = (Member) session.getAttribute("loginMember");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String memberId = userDetails.getUsername();
		log.debug("memberId : " + memberId );
		comment.setWriter(memberId);
		int result = localService.insertComment(comment);
		//log.debug("Commentresult : " + result);
		if(result > 0) {
			redirectAttr.addFlashAttribute("msg", "댓글을 등록했습니다.");
			
		}
		
		return "redirect:/local/localDetail.do?no="+comment.getLocalNo();
	}
	
	//댓글 목록
	@RequestMapping("/commentList.do")
	public ModelAndView commentList(@RequestParam int no, ModelAndView mav) {
		List<LocalCommentEntity> commentList = localService.commentList(no);
		// 뷰 이름 지정
		mav.setViewName("local/replyList");
		// 뷰에 전달할 데이터 지정
		mav.addObject("commentList",commentList);
		
		return mav;
	}
	
	//댓글 목록(json 방식 , 데이터를 리턴)
	@RequestMapping("/listJson.do")
	@ResponseBody
	public List<LocalCommentEntity> listJson(@RequestParam int no){
		List<LocalCommentEntity> commentList = localService.commentList(no);
		return commentList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@ResponseBody
	@GetMapping("/getDongDong.do")
	public Map<String, Object> getDongDong(@RequestParam int dongNo) {
		
		List<Dong>  dongguname  = memberService.selectMydongGuName(dongNo);
		
		Map<String,Object> map = new HashMap<>();
		map.put("guName", dongguname.get(0).getGu());
		map.put("dongName", dongguname.get(0).getDongName());
		log.debug( "■ dongguName : " + map );
		
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//--------------- 하나 시작 ---------------------------------------
	// 게시글 가져오기
	@GetMapping("/myLocal.do")
	public void local(Authentication authentication, Model model) {
		// member  
		Member member = ((Member)authentication.getPrincipal());
		log.debug("member = {} ", member);
		
		List<Local> myLocal = localService.selectLocalList(member);
		
		log.debug("myLocal = {}",myLocal);
		
		model.addAttribute("myLocal",myLocal);
	}
	// 댓글 가져오기
	@GetMapping("/myLocalComment.do")
	public void localComment(Authentication authentication, Model model) {		
		String memberId = ((Member)authentication.getPrincipal()).getMemberId();
		
		List<LocalCommentEntity> myLocalComment = localService.selectLocalCommentList(memberId);
		
		
		log.debug("myLocalComment = {}",myLocalComment);
		
		model.addAttribute("myLocalComment",myLocalComment);
	}
	
	//-------------- 하나 끝 ------------------------------------------
	
	
	
}

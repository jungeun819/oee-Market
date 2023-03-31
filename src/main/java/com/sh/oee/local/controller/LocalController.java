package com.sh.oee.local.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sh.oee.common.OeeUtils;
import com.sh.oee.local.model.dto.Local;

import com.sh.oee.local.model.dto.LocalAttachment;

import com.sh.oee.local.model.dto.LocalComment;
import com.sh.oee.local.model.dto.LocalEntity;
import com.sh.oee.local.model.service.LocalService;
import com.sh.oee.member.model.dto.Member;

import com.sh.oee.member.model.service.MemberService;

import java.io.File;
import java.io.IOException;

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
	public void localList(Model model , HttpSession session, Authentication authentication) {
	
		//글 목록
		List<String> myDongList = (List<String>)session.getAttribute("myDongList");
		log.debug("dongList = {}",myDongList);
		
		List<Map<String,String>> localCategory = localService.localCategoryList();
		List<Local> localList = localService.selectLocalListByDongName(myDongList);
		//동네정보 가져오기
		log.debug("localList = {}", localList);
		log.debug("localCategory = {} ", localCategory);
		
		//아이디 가져오기
		Member member =((Member)authentication.getPrincipal());
		log.debug("writeMemebr = {}", member);
//		
		
		
		//view단
		model.addAttribute("localList", localList);
		model.addAttribute("localCategory",localCategory);
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
	public void localDetail(@RequestParam(defaultValue="")String category,@RequestParam int no, Model model) {
		Local localdetail = localService.selectLocalOne(no);
		
		localdetail.setContent(OeeUtils.convertLineFeedToBr(OeeUtils.escapeHtml(localdetail.getContent())));
		
		log.debug("localdetail : {}", localdetail);
		
		//조회수 증가
		int readCnt = localService.hits(no);
		
		//좋아요 누르기 확인 -- 됐으면
		List<Map<String,Object>> likecheckMap = localService.likecheck();
		if(likecheckMap == null) {
			//사용자가 좋아요 누른 적 없으면 null 반환
			model.addAttribute("likecheck",0);
		} else {
			model.addAttribute("likecheck",likecheckMap);
		}
		
		model.addAttribute("localdetail",localdetail);
		model.addAttribute("category", category);
	}
	
	// 글 수정하기 폼 이동
	@GetMapping("/localUpdate.do")
	public void localUpdate(@RequestParam int no,Model model) {
		log.debug("no={}",no);
		
		Local localdetail = localService.selectLocalOne(no);
		List<Map<String,String>> localCategory = localService.localCategoryList();
		log.debug("localdetail ={}",localdetail);
		
		model.addAttribute("localdetail",localdetail);
		model.addAttribute("localCategory", localCategory);
		
	}
	
	// 글 수정하기
	@PostMapping("/localUpdate.do")
	public String localUpdate(Local local,
			RedirectAttributes redirectAttr) {

		int result =  localService.updateLocalBoard(local);
		
		redirectAttr.addFlashAttribute("msg","게시글이 수정됐습니다.");
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
		
		List<LocalComment> myLocalComment = localService.selectLocalCommentList(memberId);
		
		
		log.debug("myLocalComment = {}",myLocalComment);
		
		model.addAttribute("myLocalComment",myLocalComment);
	}
	
	//-------------- 하나 끝 ------------------------------------------
	
	
	
}

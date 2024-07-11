package com.avi6.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/")
@RequiredArgsConstructor//서비스 주입 @
public class BoardController {

	//서비스 이용해서 CRUD를 해야하니 필드선언, 자동 주입 받아야하므로 반드시 private final 로 할 것
	private final BoardService boardService;
	
	//글삭제 실행 매핑
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes redirectAttributes) {
		
		boardService.delArticle(bno);
		
		redirectAttributes.addFlashAttribute("msg", bno + " 글이 삭제되었습니다");
		return "redirect:/board/list";
	}
	
	//글수정 실행 매핑. 수정된 후 글번호가 있는 페이지로 Redirect 시킴
	@PostMapping("/modify")
	public String modify(BoardDTO boardDTO, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {
		System.out.println("글 수정 요청 : " + boardDTO);
		
		boardService.updateArticle(boardDTO);
		
		//넘겨주는 page 넘버 등을 생성 후 리다이렉트함.
		redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
		
		redirectAttributes.addAttribute("bno", boardDTO.getBno());
		
		return "redirect:/board/read";
	}
	
	//글수정 폼 매핑
	@GetMapping("/modify")
	public void modify(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, @RequestParam("bno") Long bno, Model model) {
		System.out.println("글 수정 요청 번호 : " + bno);
		
		BoardDTO boardDTO = boardService.get(bno);
		
		model.addAttribute("dto", boardDTO);
	}
	
	
	//글상세 요청을 처리하는 메서드
	@GetMapping("/read")
	public void read(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, @RequestParam("bno") Long bno, Model model) {
		System.out.println("글 상세 요청 : " + bno);
		
		BoardDTO boardDTO = boardService.get(bno);
		
		model.addAttribute("dto", boardDTO);
	}
	
	//새 글 작성 폼 매핑 요청 처리
	@GetMapping("/register")
	public void register() {
		System.out.println("새글 작성 요청처리함");
	}
	
	//신규글 저장처리
	@PostMapping("/register")
	public String register(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
		System.out.println("신규글 작성 DTO --> " + boardDTO);
		
		
		
		Long bno = boardService.register(boardDTO);
		
		redirectAttributes.addFlashAttribute("msg", "신규글" + bno + " 가 정상 등록되었습니다.");
		return "redirect:/board/list";
		
	}
	
	@GetMapping("/")
	public String list() {
		return "redirect:/board/list";
	}
	
	//list get 요청 매핑
	@GetMapping("/list")
	public void list(PageRequestDTO pageRequestDTO, Model model) {
		//여기서 해야할 것은 리스트 페이지의 항목을 모델에 실어서 Viewer 에 넘기는 것임.
		model.addAttribute("result", boardService.getList(pageRequestDTO));	
	}
	
}

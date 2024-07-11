package com.avi6.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.dto.PageResultDTO;

import jakarta.transaction.Transactional;



// Board Service Layer Unit Test class
@SpringBootTest
public class BoardServiceTests {

	@Autowired
	private BoardService boardService;
	
	//@Test
	void getDetail() {
		System.out.println(boardService.get(10L));
	
}
	// 지우는거
	//@Test
	void delArticle() {
		boardService.delArticle(10L);
	}
	
	
	//수정
	@Test
	
	void boardUpdateTest() {
		BoardDTO boardDTO = BoardDTO.builder()
					.bno(1L)
					.title("이건 수정본")
					.content("-----------")
					.build();
		boardService.updateArticle(boardDTO);
					
	}
	
	
	
	
	//@Test// list 항목 얻어내기 TEST
	void getBoardList() {
		PageRequestDTO pageRequestDTO = new PageRequestDTO();// 1페이지 10 개 항목 요청 생성
		pageRequestDTO.setPage(2);// 페이지가 바뀜
		
		PageResultDTO<BoardDTO, Object[]> res = boardService.getList(pageRequestDTO);
		
		for(BoardDTO boardDTO : res.getDtoList()) {
			System.out.println("이거" + boardDTO);
		}
	}
	
	
	
	
	
	//@Test
	void registerTest() {
		BoardDTO boardDTO = BoardDTO.builder()
					.title("이건 신규 등록 테스트 제목글")
					.content("이건 신규 등록 테스트 내용글")
					.writerEmail("user77@abc.com")
					.build();
		
		boardService.register(boardDTO);
		
	}
}

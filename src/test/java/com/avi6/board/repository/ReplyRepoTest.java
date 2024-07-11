package com.avi6.board.repository;
/*
 * 댓글 밀어넣기
 * 
 * 여기서는 참조가 board 와 1 : N 관계로 bno 로 관계설정이 되어 있음.
 * 
 * 따라서 없는 bno 인 경우엔 댓글이 들어가지 않음을 확인하고,
 * 
 * 또 하나는 하나의 제목에 하나 이상의 댓글이 존재하도록 랜덤을 사용해서 제목글을 랜덤하게 발생하도록 함.
 */

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.board.entity.Board;
import com.avi6.board.entity.Reply;

@SpringBootTest
public class ReplyRepoTest {
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Test//제목삭제시 댓글 삭제 테스트
	void delSubjectAndRep() {
		replyRepository.deleteByBno(2L);
	}
	
	//@Test//댓글 목록 가져오기
	void getRepList() {
		List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(95L).build());
		
		result.forEach(r -> {
			System.out.println("reply --> " + r);
		});
	}
	
	
	//@Test
	void replyInsert() {
		IntStream.rangeClosed(1, 300).forEach(i -> {
			long bno = (long)(Math.random() * 100) + 1;
			
			Board board = Board.builder()
					.bno(bno)
					.build();
			
			Reply reply = Reply.builder()
					.text("댓글(Reply)..." + i)
					.board(board)
					.replyer("guest")
					.build();
			
			replyRepository.save(reply);
		});
	}
}

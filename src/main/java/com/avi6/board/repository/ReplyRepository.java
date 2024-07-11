package com.avi6.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.avi6.board.entity.Board;
import com.avi6.board.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

	
	// 게시글 삭제 선언
	// 아래쿼리는 제목글 삭제시 댓글 삭제 쿼리임
	@Modifying
	@Transactional
	@Query("delete from Reply r where r.board.bno =:bno")
	void deleteByBno(@Param("bno") Long bno);
	
	// List 페이지에서 댓글을 보여줄 댓글 List 메서드 선언
	List<Reply> getRepliesByBoardOrderByRno(Board board);// 쿼리 메서드입니다.
}

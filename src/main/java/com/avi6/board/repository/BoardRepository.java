package com.avi6.board.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.avi6.board.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
	
	// 글 삭제 메서드 정의
	@Modifying // JPQL 의 update / delete 시엔 반드시 선언해야함
	@Query("Delete from Reply r Where r.board.bno = :bno")
	void delByBno(@Param("bno") Long bno);
	
	
	
	
	
   
   //특정 게시물의 정보를 얻어내도록 JPQL 을 이용해서 사용하는 방법 알아보기.
   //하나의 게시물에는 0 ~ n 개 까지의 댓글들이 같이 나올 수 있음.
   //이런 경우엔 하나의 Row(게시글)에 할당된 댓글들을 모두 Object[]로 리턴받도록 함.
   //주의!!! 메서드에서 리턴되는 객체는 Object 이고, 이 내부에 실제 Row 가 Object[]로 생성됨.
   //JPQL
   @Query("SELECT b, w   FROM Board b LEFT OUTER JOIN b.writer w WHERE b.bno =:bno")// =: 표시는 우측의 값을 Parameter 로 처리하겠다는 뜻
   //이런 경우엔 메서드에서 해당 이름을 @Param("bno") Long bno(변수명)으로 처리해서 동적으로 값을 매핑
   
   //하위 쿼리는 Board Table 에서 참조 관계에 있는 Writer 정보만 뽑아옴.
   //때문에 on 을 이용하지 않고 조인을 걸어도 수행 됨.(On 걸어도 상관 없음)
   Object getBoardWithWriter(@Param("bno") Long bno);
   
   
   //특정 게시물과 그에 속한 댓글을 가져오는 JPQL 작성
   //아래 쿼리는 리턴이 댓글 목록을 Object 배열값으로 갖는 List 로 리턴함.
   @Query("SELECT b, r FROM Board b LEFT OUTER JOIN Reply r ON r.board = b WHERE b.bno =:bno")// =: 표시는 우측의 값을 Parameter 로 처리하겠다는 뜻
   List<Object[]> getBoardWithReply(@Param("bno") Long bno);
   
   
   //리스트 페이지에서 Pagination 을 처리할때 사용할 JPQL 작성
   //이 쿼리는 리스트 페이지에서 하나의 제목글에 댓글의 갯수를 담아서 표시하고, 댓글이 있는 항목을 클릭하게 되면 댓글이 보여지게 만듦.
   //때문에 하나의 게시글에는 몇 개의 댓글이 있는지를 파악해야 하고, 두번째로는 전체 글수를 알아야 페이징 처리를 할때,
   //Pageable 을 이용할 수 있음.
   //그런데, 현재 JPQL 을 사용하기 때문에, 반드시 집계함수 쿼리(이른바 countQuery)를 따로 작성해야함.
   //문법 : (value = "mainQuery", countQuery="집계에 사용될 쿼리")
   
   @Query(value = "SELECT b, w, COUNT(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r "
         + "   ON r.board = b "
         + "   GROUP BY b ",
         countQuery = "SELECT COUNT(b) FROM Board b")
   
   Page<Object[]> getBoardWithReplyCnt(Pageable pageable);
   
   // 특정 bno 를 파라미터로 받아서 board,writer,count(reply) 의 모든 내용을 리턴시키도록(Object 리턴)
   
   @Query(value = "SELECT b, w, COUNT(r) FROM Board b LEFT JOIN b.writer w LEFT JOIN Reply r " 
	         + "   ON r.board = b "
	         + "WHERE b.bno =:bno" ,
	         countQuery = "SELECT COUNT(b) FROM Board b")
   Object getBoardWithBno(@Param("bno") Long bno);
   
   
   
   
}









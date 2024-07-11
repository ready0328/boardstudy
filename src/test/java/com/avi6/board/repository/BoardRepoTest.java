package com.avi6.board.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.avi6.board.entity.Board;
import com.avi6.board.entity.Member;


/*
 * Board 테이블에 데이터 밀어넣기
 * 
 * 여기서 주의할 부분은 N : 1 로 참조되어있는 member 테이블의 email 컬럼임.
 * 
 * 이 값이 없거나 틀리면 데이터가 들어가지 않으므로, member 객체를 생성, email 값을 세팅후에
 * 
 * 이 값까지를 board 에 insert 해야함.
 */
@SpringBootTest
public class BoardRepoTest {
	
	//board 데이터 밀어넣기
	@Autowired
	private BoardRepository boardRepository;
	
	@Test//검색에서 PageImpl을 리턴하는 형태 테스트하기
	void testSearchPageImpl() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending().and(Sort.by("title").ascending()));
		
		Page<Object[]> res = boardRepository.searchPage("t", "제목", pageable);
		
		System.out.println("검색 결과 ---> " + res);
				
	}
	
	//@Test
	void testSearch2() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		boardRepository.searchPage("t", "신규", pageable);
	}
	
	
	//queryDSL search() 실행여부 확인
	//@Test
	void testSearch1() {
		boardRepository.search1();
	}

	
	//@Test
	void testWithBno() {
		Object res = boardRepository.getBoardWithBno(100L);
		
		Object[] objArr =  (Object[])res;
		
		System.out.println(Arrays.toString(objArr));
		
	}
	
	
	//게시판 리스트 페이지에서 사용될 List 항목 얻어내기 테스트
	//Page 를 리턴받도록 했기 때문에 Pageable 객체를 파라미터로 넘김
	//@Test
	void testWithReplyCnt() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		
		Page<Object[]> result = boardRepository.getBoardWithReplyCnt(pageable);
		
		result.get().forEach(row ->{
			Object[] objArr = (Object[]) row;
			
			System.out.println("Row --> " + Arrays.toString(objArr));
		});
	}
	
	
	//JPQL을 이용한 특정 게시글의 댓글 정보 가져오기
	//@Test
	void readBoardWithReply() {
		List<Object[]> res = boardRepository.getBoardWithReply(100L);
		
		for(Object[] objArr : res) {
			System.out.println(Arrays.toString(objArr));
		}
	}
	
	
	//JPQL 을 이용한 board 의 특정 Row 정보와 Writer 정보 얻어내기
	//@Test
	void readBoardWithWriter() {
		Object res = boardRepository.getBoardWithWriter(100L);
		
		Object[] arr = (Object[])res;
		
		System.out.println("---------------------------------");
		System.out.println(Arrays.toString(arr));
	}
	
	//참조객체(board --> member, reply --> board)를 ManytoOne 으로 선언할때
	//Eager loading(포괄(join) select) 와 Lazy loading 의 차이점 알아보기
	
	//@Transactional//Fetch Type 이 Lazy 인 경우, 연관된 Entity 까지 조회를 하려면
	//위 @을 선언해야함. 선언을 해야 각각의 Entity 를 개별적으로 조회를 하게 되고,
	//걸지 않으면 board 의 Entity 만 조회를 하고, session(Connection)을 끊게 됨.
	//이 connection 을 재연결해서 다른 참조 Entity 까지 select 하게 하는 @임.
	//@Test
	void testReadEager() {
		//board 에서 특정 bno 의 Entity 를 get 할때, Eager loading 으로 처리되는 쿼리를 눈여겨 봄.
		Optional<Board> res = boardRepository.findById(100L);
		Board board = res.get();
		
		System.out.println("리턴된 board Row --> " + board);
		System.out.println("작성자 --> " + board.getWriter());
	}
	
	//@Test
	void boardInsert() {//100건의 사용자 정보 넣기
		IntStream.rangeClosed(1, 100).forEach(i -> {
			//Member 객체 생성 및 email 값 세팅
			Member writer = Member.builder()
					.email("user" + i + "@abc.com")
					.build();
			
			//Board 가라 데이터 생성하기
			Board board = Board.builder()
					.title("제목(title) " + i)
					.content("내용(content) ~~" + i)
					.writer(writer)
					.build();
			
			boardRepository.save(board);
		});
	}

}

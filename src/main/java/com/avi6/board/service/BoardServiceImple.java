package com.avi6.board.service;

import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.dto.PageResultDTO;
import com.avi6.board.entity.Board;
import com.avi6.board.entity.Member;
import com.avi6.board.repository.BoardRepository;
import com.avi6.board.repository.SearchBoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImple implements BoardService {
	
	//모든 repository 는 private final 로 선언하고 @을 이용해서 객체 주입을 받음.
	private final BoardRepository boardRepository;
	
	@Override
	public Long register(BoardDTO boardDTO) {
		System.out.println("전달될 DTO -->" + boardDTO);
		
		//DTO --> Entity 변환
		Board board = dtoToEntity(boardDTO);
		
		boardRepository.save(board);
		
		return board.getBno();
	}

	@Override
	public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
		//이 메서드에서는 List 를 추출하는 repository 의 메서드를 호출함
		//이때 사용자의 요청 페이지와 페이지 번호, 갯수 등은 모두 pageRequestDTO에 담겨지게 됨.
		//이 정보를 이용해서 요청된 페이지 번호와 이에 따른 글 목록, 그리고 page index 도 함게 넘어가게 처리함.
		//PageResultDTO 객체를 생성해서 넘겨주면 위 정보가 모두 계산되어져서 필드에 매핑되기 때문에
		//viewer 에서는 메서드 호출만으로 페이징, 페이지번호, 글 등을 모두 표현할 수 있음.
		//하지만, 제네릭으로 BoardDTO와 Object[]를 파람으로 받기 때문에
		//객체를 생성할때 Object 배열을 BoardDTO로 변환하는 기능(함수(function))이 필요함.
		//이 기능은 service 인터페이스에서 entityToDTO(param)으로 정의했기 때문에
		//이를 Function 객체로 만들어서 넘겨주면 됨.
		System.out.println("게시판 list 요청 처리 --> " + pageRequestDTO);
		
		//Entity --> DTO로 변환하는 Function 객체를 생성해서 PageResult 객체의 생성자로 넘겨야함.
		//한 사용자가 요청한 페이지의 페이지 정보(페이지 번호, 목록수 등..)을 처리한 boardRepository의 
		//get....() 메서드를 호출해서 처리된 Page 객체도 얻어냄.
		//위 두개를 PageResult 객체의 생성자로 넘기면서 처리된 내용을 담은 객체를 얻어냄.
		//repository 에는 2개의 Entity(Board, Member)와 리플 갯수를 담은 3개의 값이 각 Row 에
		//배열로 담겨있기 때문에 이 값을 DTO에 변환하기 위해서는 위 3개의 값을 분리해서 호출해야함.
		
		//java.util.Function interface 사용법
		//T,R 타입을 제네릭으로 선언해서 이를 구현한 실제 메서드의 구현체를 대입
		Function<Object[], BoardDTO> converFunction = (entity -> entityToDTO((Board)entity[0], (Member)entity[1], (Long)entity[2]));
		
		//위에서 Function 객체를 생성했으니, 이번엔 Page 객체를 얻어냄.
		
		//아래 List 의 목록만을 담고 있는 Page 리턴을 대신해서 키워드 검색까지 수행하는 메서드를 호출해서 처리하도록 함.
		//Page<Object[]> result = boardRepository.getBoardWithReplyCnt(pageRequestDTO.getPageable(Sort.by("bno").descending()));
		
		//키워드 검색을 포함한 리스트 항목 얻어내기
		Page<Object[]> result = boardRepository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending()));
		
		//PageResult 객체를 생성하기 위한 생성자 Param 2개를 모두 만들었으니 PageResult 객체를 생성해서
		//페이지 정보, entity --> dto 로 변환된 목록, 그리고 reple 개수를 모두 담은 PageResult 객체를 생성해서 넘김.
		return new PageResultDTO<>(result, converFunction);
		
	}

	
	@Override
	public BoardDTO get(Long bno) {
		System.out.println("글 상세 요청함 글 번호 --> " + bno);
		
		Object obj = boardRepository.getBoardWithBno(bno);
		
		Object[] arr = (Object[])obj;
		
		return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
	}

	
	@Override
	@Transactional//하나 이상의 세션이 필요한 부분에 꼭 넣기
	public void delArticle(Long bno) {
		//댓글부터 삭제
		boardRepository.delByBno(bno);
		
		//제목글 삭제는 JPA 메서드를 이용해서 삭제
		boardRepository.deleteById(bno);
	}

	
	@Override
	@Transactional
	public void updateArticle(BoardDTO boardDTO) {
		//만약 수정이 안 될 경우엔 @Transaction 추가해 
		//dto 의 수정된 내용을 바로 save 함.
		//Entity 를 얻어내고, 내용을 dto 에서 담긴 내용으로 변경함.
		//이렇게 하게 되면, 원본 Entity(Bean Proxy -> Bean 을 관리하는 메모리)가 변경됨을 인지하고 update 처리함.
		Board board = boardRepository.getReferenceById(boardDTO.getBno());
		
		board.setTitle(boardDTO.getTitle());
		board.setContent(boardDTO.getContent());
		
		boardRepository.save(board);
	}
}

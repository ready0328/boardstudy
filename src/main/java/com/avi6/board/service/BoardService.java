package com.avi6.board.service;
/*
 * 컨트롤러에서 사용할 서비스 레이어의 Main
 * 
 * 여기서는 사용할 Model 에 넘겨질 데이터를 CRUD 하는 Repository 를 호출하여 결과를 Model 에 매핑하는 작업을 함.
 * 
 * 실제 구현 내용은 얘를 상속한 구현체가 할 예정이고, 메서드를 선언만하면 됨.
 * 
 * 또한, DTO --> Entity, Entity --> DTO 로 변환해야 하는 기본 메서들도 정의함.
 */

import com.avi6.board.dto.BoardDTO;
import com.avi6.board.dto.PageRequestDTO;
import com.avi6.board.dto.PageResultDTO;
import com.avi6.board.entity.Board;
import com.avi6.board.entity.Member;

public interface BoardService {

	//가장 기본적인 등록 메서드 선언
	//신규 등록후 bno 리턴시키는 메서드
	Long register(BoardDTO boardDTO);
	
	//DTO --> Entity 변화 default method 정의
	//Board 에는 Member 를 참조하고 있기 때문에, member 에 필요한 정보인 email(작성자) 정도만
	//DTO 에서 get 후 Member 객체 샛업.
	default Board dtoToEntity(BoardDTO boardDTO) {
		Member member = Member.builder()
				.email(boardDTO.getWriterEmail())
				.build();
		
		Board board = Board.builder()
				.bno(boardDTO.getBno())
				.title(boardDTO.getTitle())
				.content(boardDTO.getContent())
				.writer(member)//필드 타입이 Member 이므로 위에서 정의한 member 객체를 줘야함.
				.build();
		
		return board;
	}
	
	//이번엔 Entity --> DTO로 변환하는 default 메서드 정의
	//이전에 했던 guestBook 메서는 JPQL을 이용하지 않았기 때문에
	//Repo 의 Select 의 리턴타입이 특정 타입으로 지정되었지만, 이번엔 모두 Object 배열에 담겨 옴.
	//따라서 이 배열의 내용을 각 DTO에 매핑을 해줘야 함.
	//이러려면 객체가 필요한데, 이 객체를 메서드의 파라미터로 넘겨서 처리하도록 하고, 메서드에서는
	//배열내의 데이터 중 각 DTO에 필요한 내용을 매핑하도록 할거임.
	default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {
		BoardDTO boardDTO = BoardDTO.builder()
				.bno(board.getBno())
				.title(board.getTitle())
				.content(board.getContent())
				.modDate(board.getModDate())
				.regDate(board.getRegDate())
				.writerEmail(member.getEmail())
				.writerName(member.getName())
				.replyCount(replyCount.intValue())
				.build();
		
		return boardDTO;
			
	}
	
	//List 페이지에서 뿌려질 게시판의 특정 페이지의 항목을 리턴받는 메서드 선언
	//모델로 넘기도록 처리 예정이라 PageResultDTO 타입으로 리턴시킴.
	PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO);
	
	//글상세를 위한 메서드 선언(당연히 글 번호 필요)
	BoardDTO get(Long bno);
	
	//글삭제 메서드
	void delArticle(Long bno);
	
	//글수정 메서드
	void updateArticle(BoardDTO boardDTO);
	
}

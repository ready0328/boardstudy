package com.avi6.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.avi6.board.entity.Board;

public interface SearchBoardRepository {
	
	Board search1();//테스트용
	
	//키워드 검색시, 제목 or 내용 or 작성자 위주로 검색(방명록 기준)
	//게시판 역시 같은 키워드 검색 내용을 적용할거임. 리스트에 위 파라미터가 존재할수도, 안할수도 있음.
	//만약 존재한다면, 검색 내용이 있을수도(1 이상), 없을수도 있으며(0), 만약 1이상이라면, 10개 이상이 될 수도 있음.
	//때문에, 검색에서도 Pagenation 이 필요할 수 있고 이를 처리할거임.

	//하위 메서드는 위 내용이 모두 처리된 목록을 가지고 있는 Page<Object[]>를 리턴하도록 메서드 선언함.
	public Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}

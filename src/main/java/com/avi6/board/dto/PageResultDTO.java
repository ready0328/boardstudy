package com.avi6.board.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 이 페이지는 요청된 페이지에 따른 페이징을 리턴하는 클래스로,
 * 여기서 Java Function 객체를 이용하는데, 얘는 stream 을 이용할때 mapToxxx()의 파라미터로
 * 이 객체를 구현한 객체를 받기 때문에 사용함.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResultDTO<DTO, EN> {//Generic 선언

	//Select 시 넘겨줄 DTO 목록 Collection
	private List<DTO> dtoList;
	
	//총 페이지 번호
	private int totalPage;
	
	//현재 페이지 번호
	private int page;
	
	//글목록 size
	private int size;
	
	//시작, 끝 페이지 변수
	private int start, end;
	
	//이전, 다음 변수
	private boolean prev, next;
	
	//리스트 페이지에 페이징 ([1][2][3]...) 등을 넘길 변수
	private List<Integer> pageList;
	
	public PageResultDTO(Page<EN> result, Function<EN,DTO> function) {
		dtoList = result.stream().map(function).collect(Collectors.toList());
		totalPage = result.getTotalPages();
		makePageList(result.getPageable());
	}
	
	private void makePageList(Pageable pageable) {
		this.page = pageable.getPageNumber() + 1;
		this.size = pageable.getPageSize();
		
		int tempEnd = (int)(Math.ceil(page / 10.0)) * 10;
		
		start = tempEnd - 9;
		prev = start > 1;
		end = totalPage > tempEnd ? tempEnd : totalPage;
		next = totalPage > tempEnd;
		
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		
	}

}
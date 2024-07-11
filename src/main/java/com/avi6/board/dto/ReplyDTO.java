package com.avi6.board.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReplyDTO {
	
	private Long rno;
	
	private String text;
	
	private String replyer;
	
	private Long bno;//참조 제목글 번호
	
	private LocalDateTime regDate;
	
	private LocalDateTime modDate;
}

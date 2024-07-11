package com.avi6.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 댓글에 관련된 Entity
 * 
 * 칼럼으로는 댓글 번호, 내용, 작성자, 게시물 번호만 둘거임.
 * 
 * 이중 게시물 번호는 board 클래스를 참조해야만 함. (N : 1)관계
 * 
 * 따라서 board 를 필드로 선언하고, 필요한 정보를 join 을 통해 얻어냄.
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Reply extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	
	private String text;
	
	private String replyer;
	
	//글번호를 참조할 필드 선언
	@ManyToOne(fetch = FetchType.LAZY) //1:N 관계로 테이블을 생성
	@ToString.Exclude
	private Board board;
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}
	

}

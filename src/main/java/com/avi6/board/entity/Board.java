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
 * 이 클래스는 게시판의 주 내용을 담는 애임.
 * 
 * 이 중 눈여겨 볼 부분은 작성자 이메일을 참조하는 Member 필드임.
 * 
 * 이 필드를 이용해서 insert or select 시, 같이 join 이 걸리도록 함.
 * 
 * 테이블 컬럼은 글번호, 제목, 내용, 작성자 로 할거임.
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Board extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bno;//글번호
	
	private String title;
	
	private String content;
	
	//작성자 필즈는 Member 객체로
	//하위 객체를 부모로, 1:N 관계로 테이블을 생성하도록 하는 annotation.
	@ManyToOne(fetch = FetchType.LAZY) 
	//fetch Type 은 default 가 Eager 임.(만약 Lazy 로 선언시엔, Repository 메서드에서 @Transaction 을 선언해야 함.
	//ToString 같은 경우, 참조되는 객체정보까지 (기본으로)나오는데, 이를 제한하는 @가 있음.
	@ToString.Exclude
	private Member writer;
	
	//수정을 위한 두 개의 setter 만 추가함.
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}

package com.avi6.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

	//회원정보는 email(pk), password, name 정도만 할거임
	@Id
	private String email;//얘가 pk 인데, 문자열로 처리할거면 따로 pk 전략 사용 X
	
	private String password;
	
	private String name;
	
}

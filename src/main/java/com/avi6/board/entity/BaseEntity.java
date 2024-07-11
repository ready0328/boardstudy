package com.avi6.board.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/*
 * Base Entity 는 추상클래스로 선언.
 */
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})//이렇게 선언하면, Main 클래스에 @EnableJpaAuditing 을 반드시 선언해야함.
@Getter
public class BaseEntity {

	//회원가입일, 작성일, 수정일 등을 자동관리하도록 필드 선언함.
	
	@CreatedDate
	@Column(name = "regdate", updatable = false)//컬럼명 설정 및 수정 금지
	private LocalDateTime regDate;
	
	@LastModifiedDate
	@Column(name = "moddate")
	private LocalDateTime modDate;

}

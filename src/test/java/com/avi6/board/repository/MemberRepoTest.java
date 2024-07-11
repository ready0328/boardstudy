package com.avi6.board.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.board.entity.Member;

@SpringBootTest
public class MemberRepoTest {

	//member 테이블에 데이터 밀어넣기
	@Autowired
	private MemberRepository memberRepository;
	
	@Test
	void memberInsert() {//100건의 사용자 정보 넣기
		IntStream.rangeClosed(1, 100).forEach(i -> {
			Member member = Member.builder()
					.email("user" + i + "@abc.com")
					.name("user")
					.password("1111")
					.build();
			
			memberRepository.save(member);
		});
	}
	
}

package com.avi6.board.repository.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.avi6.board.entity.Board;
import com.avi6.board.entity.QBoard;
import com.avi6.board.entity.QMember;
import com.avi6.board.entity.QReply;
import com.avi6.board.repository.SearchBoardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

	/*
	 * QueryDSL을 이용한 JPQL을 사용할 때는 반드시 아래의 규칙 몇 가지를 따라야 함.
	 * 
	 * 1. Repository interface 를 반드시 부모로 가져야함. 여기선 SearchBoardRepository
	 * 
	 * 2. 실제 구현 클래스명은 위에서 선언한 부모 interface 이름 + Impl 로 해야함.
	 * 
	 * 3. 실제 지원 클래스는 QuerydslRepositorySupport이므로, 반드시 상속을 받아야함.
	 * 
	 * 4. 명시적으로 부모클래스에 super()를 이용해서 domain클래스 클래스를 파라미터로 넘겨야 함.
	 * 	  이 domain 클래스는 일반적으로 Entity 를 지정함.
	 */
	
	public SearchBoardRepositoryImpl() {
		super(Board.class);
	}
	
	//테스트용
	@Override
	public Board search1() {
		
		//log.info("검색 메서드 실행");
		
		//QueryDSL의 JPQL Lib 를 사용할 준비가 되었다면, 실제 DSL lib 의
		//JPQLQuery 라는 interface 를 이용해서 조인 등을 처리하게 함.
		//생성하는 방법은 일반적으로 아래와 같음
		
		//사용할 QEntity를 파라미터로 해서 JPQLQuery 객체를 생성함. 이때 from() 이라는 상속받은 메서드를 이용
		
		//생성된 JQPLQuery 객체를 이용해서 내부 메서드(쿼리를 메서드 형태로 정의한)을 이용해서 데이터를 얻어낼 수 있음.
		
		QBoard board = QBoard.board;
		QMember member = QMember.member;
		QReply reply = QReply.reply;
		
		JPQLQuery<Board> jpqlQuery = from(board);
		jpqlQuery.leftJoin(member).on(board.writer.eq(member));
		jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
		
		//tuple 객체를 리턴한 경우엔 추가 조건은 따로 처리해야함.
		JPQLQuery<Tuple> tuples = jpqlQuery.select(board, member, reply.count());//.groupBy(board);
		tuples.groupBy(board);
		
		System.out.println("-------------------------- JPQL JOIN select 결과 -------------------------");
		System.out.println(tuples);
		
		List<Tuple> res = tuples.fetch();
		
		res.forEach(t -> System.out.println(t));
		
		/*
		 * jpqlQuery.select(board).where(board.bno.eq(3L));
		 * 
		 * System.out.println("-------------------------- JPQL select 결과 -------------------------");
		 * System.out.println(jpqlQuery);
		 */
		//조인을 걸어보도록 하자
		//left, right 모든 조인을 메서드 형식으로 처리 가능
		//이때 join 을 걸 도메인(Entity)를 각각 on()이나, left, right 조인 메서드를 이용해서 수행함
		//때문에 조인이 걸려야할 도메인들 QEntity 객체를 먼저 얻어내고 수행해야함.
		
		return null;
	}

	@Override
	public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
		//Query DSL 에서 조건식을 사용해서 Page 를 리턴받을때, BooleanBuilder 와 BooleanExpress 사용했었음.
		//위 객체를 사용해서 조건식을 검색할거고, 나중에 Sort(정렬)을 지정할때는 기존과 조금 다른 API를 사용할거임.
		//또한, 쿼리에 group by 를 사용하는데, queryDSL에서는 이 절은 맨 마지막에 사용(일반 쿼리와 똑같이)
		
		QBoard board = QBoard.board;
		QMember member = QMember.member;
		QReply reply = QReply.reply;
		
		JPQLQuery<Board> jpqlQuery = from(board);
		jpqlQuery.leftJoin(member).on(board.writer.eq(member));
		jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
		
		//tuple 객체를 리턴한 경우엔 추가 조건은 따로 처리해야함.
		JPQLQuery<Tuple> tuples = jpqlQuery.select(board, member, reply.count());
		
		//조건을 처리할 BooleanBuilder, Express 를 이요해서 조건 처리를 함.
		
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		BooleanExpression exBooleanExpression = board.bno.gt(0L);//bno > 0 이상인 조건 결정
		
		//키워드 조건 추가
		booleanBuilder.and(exBooleanExpression);
		
		//키워드 타입은 one or all 일수도 있음.
		if(type != null) {
			//키워드 분리
			String[] typeArr = type.split("");
			
			//검색조건 추가
			BooleanBuilder cBuilder = new BooleanBuilder();
			
			for(String s : typeArr) {
				switch (s) {
				
				case "t"://제목 검색
					cBuilder.or(board.title.contains(keyword));
					break;
					
				case "c"://내용 검색
					cBuilder.or(board.content.contains(keyword));
					break;
					
				case "w"://작성자 검색
					cBuilder.or(member.email.contains(keyword));
					break;
	
				}
			}
			
			//조건검색 처리 완료
			booleanBuilder.and(cBuilder);
		}
		
		//위 join 에서 생성된 tuple 의 조건에 booleanBuilder add
		//이후에 group by 처리
		tuples.where(booleanBuilder);
		tuples.groupBy(board);
		
		//System.out.println("조건 검색 결과 ---> " + res);
		
		/*
		 * 마지막으로 처리할 부분이 Sort 를 처리하는 부분임
		 * JPQLQuery DSL을 이용할때는 이 Sort 처리하는 부분이 조금 다름.
		 * 
		 * 일단 처리방법만 먼저 말하면, applyPagenation() 메서드를 이용해서 처리함.
		 * 이 메서드는 Pageable, QueryDSL 의 lib 인 JPQLQuery 두 개를 파라미토로 받아서
		 * 적용된 쿼리에 해당하는 Page 를 리턴하는데, 이떄 정렬도 함께 이뤄짐.
		 * 
		 * 이를 통해서 Page<E> 를 리턴받는데, 이 Page<E>는 interface 이므로 이를 직접 재정의 해야하지만,
		 * 내부적으로 이를 정의한 PageImpl을 리턴받는 방식으로 일반적으로 사용함.
		 */
		this.getQuerydsl().applyPagination(pageable, tuples);//정렬과 Page 를 리턴받기 위한 QueryDSL 의 apply...() get..
		
		List<Tuple> res = tuples.fetch();//List<Tuple> 리턴메서드 호출
		
		//댓글 카운트 얻어내기.
		long count = tuples.fetchCount();
		
		//최종적으로 조건 및 정렬 처리된 PageImpl 객체 리턴함... 이때, 리턴된 Object[]를 List 로 변경해서 리턴시킴.
		return new PageImpl<Object[]>(res.stream().map(Tuple :: toArray).collect(Collectors.toList()), pageable, count);
		
	
		
		
		
		
	}

}

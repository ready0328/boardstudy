package com.avi6.board.controller;
/*
 * reply 관련 처리를 하는 컨트롤러(모든 reply 관련된 req, res 는 REST 형식으로 함)
 * 컨트롤러를 선언하는 @RestController 를 선언만 하면 됨. 
 * 
 * 이후 각 요청을 처리하는 메서드는 매핑 방식을 반드시 Rest 방식에 맞게끔 선언을 해야하는데,
 * 이 부분은 각 모듈(메서드)을 작성할때 알게 됨.
 * 
 * 1. 요청할때 요청 path 를 특정 값만으로만 할때가 있음. ex. /reply/2 이런식으로
 * 이걸 패스파라미터라고 함.(ㄱ!!!!) 2라는 값은 요청시마다 변경될 수 있음.
 * 여기서 숫자 '2'는 글 번호를 2로 준다는 의미이고, 이 자체가 하나의 path 를 구성함.
 * 
 * 컨트롤러에서 패스파라미터를 받도록 해야하는데, 이때 사용하는 표현법이 {bno}임.
 * 이렇게 선언하면 패스파라미터로 전달되는 요청의 값만 파싱해서 얻어낼 수 있음.
 * 
 * 2. 응답을 할때(서버가 클라이언트에게) 데이터가 만약 JSON 으로 전달 되어야할 경우엔
 * 반드시 응답전에 타입을 먼저 명시해야함. 그리고 응답하는 객체도 ResponseEntity 라는 객체를 이용함.
 * 
 * 마지막으로, 응답을 줄때는 반드시 서버가 상태코드(Status Code)와 같이 넘겨야 함.
 * 그래야 브라우저가 헤더를 분석후 데이터를 받을지, 에러를 띄울지를 결정함.
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avi6.board.dto.ReplyDTO;
import com.avi6.board.service.ReplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/replies/")
@RequiredArgsConstructor
public class ReplyController {

	//Service 선언
	private final ReplyService replyService;
	
	//제일 먼저 List 에 뿌려지는 댓글 가져오는 기능 처리
	//사용자가 게시판을 요청했기 때문에 요청 path 는 board 로 같이 함.
	//단, 이때 요청되는 댓글의 게시글 정보를 패스파라미터로 같이 보내도록 처리할거임.
	//매핑@의 속성(value, produces(클라이언트에게 전달된 데이터의 Mime Type 선언) 등)을 이용해서 요청 및 응답 데이터를 설정함. 
	//consumes(이건 요청이 올때 Data Type 을 지정된 타입으로 받겠다는 의미) 
	//produces(이건 보낼때 지정된 타입으로 보내겠다는 의미) 
	@GetMapping(value = "/board/{bno}", produces = MediaType.APPLICATION_JSON_VALUE)
	
	//JSON 으로 보낼때는 반드시 매퍼 메서드의 리턴 타입을 responseEntity로 지정을 해야함.
	//꼭 기억!!!! 패스 파라미터로 요청이 올 경우엔 이 파라미터를 받도록 @PathVariable을 선언하고 파라미터 이름을 지정해야 함.
	
	public ResponseEntity<ReplyDTO> getListByBoard(@PathVariable("bno") Long bno){
		
		return new ResponseEntity(replyService.getList(bno), HttpStatus.OK);
		
	}
	
	//신규 글 등록 메서드 정의
	//전송 데이터가 JSON, @RequestBody를 이용해서 데이터를 get 함.
	@PostMapping("")
	public ResponseEntity<Long> register(@RequestBody ReplyDTO replyDTO){
		System.out.println("신규 댓글 처리요청됨.");
		Long rno = replyService.register(replyDTO);
		
		return new ResponseEntity(rno, HttpStatus.OK);//이건주석임.
	}
	
	
	@PutMapping("/{rno}")
	public ResponseEntity<String> modify(@RequestBody ReplyDTO replyDTO){
		System.out.println("댓글 수정 요청됨.");
		replyService.modify(replyDTO);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
	@DeleteMapping("/{rno}")
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
		System.out.println("댓글 삭제 요청됨.");
		replyService.remove(rno);
		return new ResponseEntity<String>("success",HttpStatus.OK);
	}
	
}

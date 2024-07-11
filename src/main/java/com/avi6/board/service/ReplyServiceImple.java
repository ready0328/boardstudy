package com.avi6.board.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.avi6.board.dto.ReplyDTO;
import com.avi6.board.entity.Board;
import com.avi6.board.entity.Reply;
import com.avi6.board.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImple implements ReplyService {

	private final ReplyRepository replyRepository;
	
	@Override
	public Long register(ReplyDTO replyDTO) {
		
		Reply reply = dtoToEntity(replyDTO);
		
		reply = replyRepository.save(reply);
		
		return reply.getRno();
	}

	@Override
	public List<ReplyDTO> getList(Long bno) {
		Board board = Board.builder().bno(bno).build();
		List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(board);
		
		return result.stream().map(entity -> entityToDTO(entity)).collect(Collectors.toList());
	}

	@Override
	public void modify(ReplyDTO replyDTO) {
		
		Optional<Reply> res = replyRepository.findById(replyDTO.getRno());
		if(res.isPresent()) {
			Reply reply = res.get();
			reply.setText(replyDTO.getText());
			reply.setReplyer(replyDTO.getReplyer());
			replyRepository.save(reply);
		}
		
	}

	@Override
	public void remove(Long rno) {
		replyRepository.deleteById(rno);

	}

}

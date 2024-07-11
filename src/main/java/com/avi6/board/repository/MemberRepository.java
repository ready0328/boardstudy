package com.avi6.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avi6.board.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

}

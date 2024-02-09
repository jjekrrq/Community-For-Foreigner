package com.example.project.board.repository;

import com.example.project.board.domain.Board;
import com.example.project.board.domain.Hearts;
import com.example.project.member.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Transactional()
public interface HeartsRepository extends JpaRepository<Hearts, Long> {
    Optional<Hearts> findByMemberAndBoard(Member member, Board board);
}

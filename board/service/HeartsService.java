package com.example.project.board.service;

import com.example.project.board.domain.Board;
import com.example.project.board.domain.Hearts;
import com.example.project.board.repository.BoardRepository;
import com.example.project.board.repository.HeartsRepository;
import com.example.project.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class HeartsService {
    private final HeartsRepository heartsRepository;
    private final BoardRepository boardRepository;


    public boolean addHearts(Member member, Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow();

        // 중복 좋아요 방지
        if(isNotAlreadyHearts(board, member)){
            heartsRepository.save(new Hearts(board, member));
            return true;
        }
        return false;
    }


    // 사용자가 이미 좋아요 누른 게시물인지 확인
    private boolean isNotAlreadyHearts(Board board, Member member){
        return heartsRepository.findByMemberAndBoard(member,board).isEmpty();
    }


    public boolean deleteHearts(Member member, Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow();

    }


}

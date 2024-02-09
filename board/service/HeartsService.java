package com.example.project.board.service;

import com.example.project.board.domain.Board;
import com.example.project.board.domain.Hearts;
import com.example.project.board.repository.BoardRepository;
import com.example.project.board.repository.HeartsRepository;
import com.example.project.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;


@Service
@RequiredArgsConstructor
public class HeartsService {
    private final HeartsRepository heartsRepository;
    private final BoardRepository boardRepository;

    // CREATE : 좋아요 누르기
    @Transactional
    public boolean addHearts(Member member, Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new NotFoundException("Could not found board id"));

        // 중복 좋아요 방지
        if(isNotAlreadyHearts(board, member)){
            heartsRepository.save(new Hearts(board, member));
            return true;
        }
        return false;
    }


    // 사용자가 좋아요를 누르지 않았는가?
    private boolean isNotAlreadyHearts(Board board, Member member){
        return heartsRepository.findByMemberAndBoard(member,board).isEmpty();
    }
    // 사용자가 좋아요를 눌렀는가?
    private boolean isAlreadyHearts(Board board, Member member){
        return heartsRepository.findByMemberAndBoard(member, board).isPresent();
    }
    // DELETE : 좋아요 삭제하기
    @Transactional
    public boolean deleteHearts(Member member, Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new NotFoundException("Could not found board id"));

        Hearts hearts = heartsRepository.findByMemberAndBoard(member, board).orElseThrow(() ->
                new NotFoundException("Could not found heart id"));

        if(isAlreadyHearts(board, member)){
            heartsRepository.delete(hearts);
            return true;
        }
        return false;
    }


}

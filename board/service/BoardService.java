package com.example.project.board.service;

import com.example.project.board.domain.Board;
import com.example.project.board.dto.BoardRequestDto;
import com.example.project.board.dto.BoardResponseDto;
import com.example.project.board.repository.BoardRepository;
import com.example.project.member.domain.Member;
import com.example.project.member.repository.MemberRepository;
import com.example.project.reply.domain.Reply;
import com.example.project.reply.dto.ReplyResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // CREATE : 게시글 생성하기.
    @Transactional // 게시판 생성
    public void createBoard(BoardRequestDto boardRequestDto, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 사용자."));

        Board board = Board.builder()
                .boardId(boardRequestDto.getBoardId())
                .writer(member.getName()) // 현재 로그인 되어 있는 유저의 이름 저장.
                .title(boardRequestDto.getTitle())
                .contents(boardRequestDto.getContents())
                .member(member)
                .build();
        boardRepository.save(board);
    }
    // READ : 전체 게시글 조회
    @Transactional // 게시판 조회
    public List<BoardResponseDto> readBoard() {
        List<Board> readBoard = boardRepository.findAll();
        List<BoardResponseDto> boardDtoRead = new ArrayList<>();

        for (Board board : readBoard) {
            BoardResponseDto boardDto = BoardResponseDto.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .writer(board.getWriter())
                    .build();

            boardDtoRead.add(boardDto);
        }
        return boardDtoRead;
    }
    // READ : 유저 ID로 게시글 찾기 / 수정해야 함
    @Transactional
    public List<BoardResponseDto> getOneBoard(Long userId){
        List<BoardResponseDto> boardResponseDtos = boardRepository.findAll().stream()
                .filter(board -> {Member member1 = board.getMember();
                    // NullPointerException - 예외 처리를 위한 코드.
                return member1 != null && member1.getId() != null && member1.getId().equals(userId);})
                .map(board -> BoardResponseDto.builder()
                        .boardId(board.getBoardId())
                        .writer(board.getWriter())
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .build())
                .collect(Collectors.toList());

        return boardResponseDtos;
    }
    // READ : 작성자 이름으로 게시글 조회하기 / 수정해야 함
    @Transactional
    public List<BoardResponseDto> getBoardsThroughWriter(String writer){
        List<BoardResponseDto> boardResponseDtos = boardRepository.findAll().stream()
                .filter(board -> board.getWriter().equals(writer))
                .map(board -> BoardResponseDto.builder()
                        .boardId(board.getBoardId())
                        .writer(board.getWriter())
                        .title(board.getTitle())
                        .contents(board.getContents())
                        .build())
                .collect(Collectors.toList());
        return boardResponseDtos;
    }
    // READ : 게시판 ID로 게시글 조회하기 / 상세 조회(댓글까지 나오게)
    @Transactional
    public BoardResponseDto getBoardThroughBoardId(Long boardId){
        // boardId를 가지고 게시글을 조회함.
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 게시물"));

        // FetchType을 LAZY로 설정했기 때문에 트랜잭션 안에서 필요한 데이터를 미리 조회해야 함.
        // 트랜잭션 밖에서 엔티티를 사용하면 'LazyInitializationException' 발생.
        // 또는, FetchType을 EAGER로 변경. 하지만, EAGER는 성능 이슈를 야기할 수 있기 때문에 사용을 지양해야 함.
        if(board != null) {
            // 게시글과 연관된 댓글 목록 가져오기.
            List<Reply> replies = board.getReplies();

            // 게시글과 연관된 댓글 목록을 조회하려면 트랜잭션 내에서 미리 조회하거나 Fetch 전략을 수정해야 함.

            // 댓글 목록을 Dto로 변환.
            List<ReplyResponseDto> replyResponseDtos = replies.stream()
                    .map(reply -> new ReplyResponseDto(reply.getReplyId(), reply.getContent(), reply.getContent()))
                    .collect(Collectors.toList());
            // BoardResponseDto 생성.
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .boardId(board.getBoardId())
                    .writer(board.getWriter())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .replies(replyResponseDtos)
                    .build();
            return boardResponseDto;
        }
        return null;
    }
    // UPDATE : 게시글 수정하기
    @Transactional //  게시판 수정
    public BoardResponseDto updateBoard(Long boardId, BoardRequestDto boardRequestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.update(boardRequestDto);
        return BoardResponseDto.of(board);
    }
    // DELETE : 게시글 삭제하기
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        board.removeReplies();
        boardRepository.flush();
        boardRepository.delete(board);
    }
}

package com.example.project.board.service;

import com.example.project.board.domain.Board;
import com.example.project.board.domain.Hearts;
import com.example.project.board.dto.BoardRequestDto;
import com.example.project.board.dto.BoardResponseDto;
import com.example.project.board.dto.PageDto;
import com.example.project.board.repository.BoardRepository;
import com.example.project.member.domain.Member;
import com.example.project.member.repository.MemberRepository;
import com.example.project.reply.domain.Reply;
import com.example.project.reply.dto.ReplyResponseDto;
import com.example.project.reply.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;

    // CREATE : 게시글 생성하기.
    @Transactional // 게시판 생성
    public Long createBoard(BoardRequestDto boardRequestDto, Long userId) {
        Member member = memberRepository.findById(userId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 사용자."));

        Board board = Board.builder()
                .boardId(boardRequestDto.getBoardId())
                .writer(member.getName()) // 현재 로그인 되어 있는 유저의 이름 저장.
                .title(boardRequestDto.getTitle())
                .contents(boardRequestDto.getContents())
                .region(boardRequestDto.getRegion())
                .member(member)
                .build();
        boardRepository.save(board);
        return board.getBoardId();
    }
    // READ : 전체 게시글 조회
    @Transactional // 게시판 조회
    public List<BoardResponseDto> readBoard() {
        List<Board> readBoard = boardRepository.findAll();
        List<BoardResponseDto> boardDtoRead = new ArrayList<>();

        for (Board board : readBoard) {
            List<Reply> replies = board.getReplies();
            List<ReplyResponseDto> replyResponseDtos = replies.stream()
                    .map(reply -> new ReplyResponseDto(reply.getReplyId(), reply.getContent(), reply.getContent()))
                    .toList();

            BoardResponseDto boardDto = BoardResponseDto.builder()
                    .boardId(board.getBoardId())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .writer(board.getWriter())
                    .region(board.getRegion())
                    .replies(replyResponseDtos)
                    .theNumberOfReply((long) replies.size())
                    .build();

            boardDtoRead.add(boardDto);
        }
        return boardDtoRead;
    }
    // READ : 전체 게시글 조회 (페이징 처리)
    // Pageable : 페이징 처리를 도와주는 인터페이스 / 인자로 "조회하고자 하는 페이지", "한 페이지당 할당 할 게시글 개수", "정렬 방법" 을 줌.
    // 반환형이 Page<T> or PageDto<T>일 때, 인자로 무조건 Pageable형을 넣어줘야 함.
    @Transactional
    public PageDto<BoardResponseDto> readAllBoardWithPaging(Pageable pageable){
        int requestedPageNumber = pageable.getPageNumber();
        int totalNumberOfPages = getTotalNumberOfPages(pageable.getPageSize());

        if(requestedPageNumber <= totalNumberOfPages) {
            // 전체 Board 엔티티 조회
            Page<Board> boardPage = boardRepository.findAll(pageable);

            // getContent() : 현재 페이지의 정보 가져오기. 즉, 현재 페이지의 게시글들 가져오기.
            List<BoardResponseDto> boardDtoRead = boardPage.getContent().stream()
                    // 가져온 게시글 정보를 스트림 변환 후, Dto로 변환.
                    .map(board -> {
                        List<Reply> replies = board.getReplies();
                        List<ReplyResponseDto> replyResponseDtos = replies.stream()
                                // Reply 엔티티를 Dto로 변환. 엔티티 보호를 위해.
                                .map(reply -> new ReplyResponseDto(reply.getReplyId(), reply.getContent(), reply.getContent()))
                                .toList();
                        // 게시글 전달 Dto로 변환
                        return BoardResponseDto.builder()
                                .boardId(board.getBoardId())
                                .title(board.getTitle())
                                .writer(board.getWriter())
                                .contents(board.getContents())
                                .region(board.getRegion())
                                .replies(replyResponseDtos)
                                .theNumberOfReply((long) replies.size())
                                .createdDate(board.getCreatedDate())
                                .view((long)board.getView())
                                .hearts(board.getHearts().stream().count())
                                .build();
                    })
                    .toList();
            return new PageDto<>(boardDtoRead, boardPage.getTotalPages(), boardPage.getTotalElements(), boardPage.getNumber());
        }
        else {
            // 페이지 초과했을 때, 전달할 오류 메세지.
            String errorMessage = "Requested page number exceeds total number of pages.";
            return new PageDto<>(Collections.emptyList(), totalNumberOfPages, 0, requestedPageNumber, errorMessage);
        }
    }
    // 페이징 처리를 위한 메소드 작성
    private int getTotalNumberOfPages(int pageSize){
        // 전체 항목 수를 조회하는 메소드
        long totalNumberOfBoards = boardRepository.count();
        // 총 게시글 개수 / 한 페이지안에 들어갈 수 있는 게시글 개수 = 총 페이지 개수
        return (int) Math.ceil((double) totalNumberOfBoards / pageSize);
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
            List<Hearts> hearts = board.getHearts();
            // 조회수를 1씩 증가하게 만듦.
            board.setView(board.getView() + 1);

            // 게시글과 연관된 댓글 목록을 조회하려면 트랜잭션 내에서 미리 조회하거나 Fetch 전략을 수정해야 함.

            // 댓글 목록을 Dto로 변환.
            List<ReplyResponseDto> replyResponseDtos = replies.stream()
                    .map(reply -> new ReplyResponseDto(reply.getReplyId(), reply.getContent(), reply.getWriter()))
                    .collect(Collectors.toList());
            Long heartsCount = (long)hearts.size();
            // BoardResponseDto 생성.
            BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                    .boardId(board.getBoardId())
                    .writer(board.getWriter())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .region(board.getRegion())
                    .replies(replyResponseDtos)
                    .theNumberOfReply((long)replyResponseDtos.size())
                    .view((long)board.getView())
                    .hearts(heartsCount)
                    .createdDate(board.getCreatedDate())
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

package com.example.project.reply.service;

import com.example.project.board.domain.Board;
import com.example.project.reply.domain.Reply;
import com.example.project.reply.dto.ReplyRequestDto;
import com.example.project.board.repository.BoardRepository;
import com.example.project.reply.dto.ReplyResponseDto;
import com.example.project.reply.repository.ReplyRepository;
import com.example.project.member.domain.Member;
import com.example.project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    // CREATE
    @Override
    @Transactional
    public Long writeReply(ReplyRequestDto replyRequestDto, Long boardId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("존재하지 않는 사용자."));
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Reply reply = Reply.builder()
                // 추가
                .writer(member.getName())
                .content(replyRequestDto.getContent())
                .member(member)
                .board(board)
                .build();
        replyRepository.save(reply);

        // 댓글 ID
        return reply.getReplyId();
    }

    // READ
    @Override
    @Transactional
    public List<ReplyResponseDto> getReplyLists(Long boardId) {
        List<ReplyResponseDto> replyResponseDtos = replyRepository.findAll().stream()
                .filter(reply -> {Board board = reply.getBoard();
                return board != null && board.getBoardId() != null && board.getBoardId().equals(boardId);})
                .map(reply -> ReplyResponseDto.builder()
                        .replyId(reply.getReplyId())
                        .contents(reply.getContent())
                        .writer(reply.getWriter())
                        .build())
                .collect(Collectors.toList());
        return replyResponseDtos;
    }

    // UPDATE
    @Override
    @Transactional
    public ReplyResponseDto updateReply(Long replyId, ReplyRequestDto replyRequestDto) {
        Reply reply = replyRepository.findById(replyId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다."));
        reply.update(replyRequestDto);
        return ReplyResponseDto.of(reply);
    }

    // DELETE
    @Override
    public void deleteReplyById(Long replyId) {
        // 예외 처리 / 댓글 ID를 통해 댓글의 존재 유무 파악.
        Reply reply = replyRepository.findById(replyId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 댓글입니다."));
        replyRepository.deleteById(replyId);
    }
}
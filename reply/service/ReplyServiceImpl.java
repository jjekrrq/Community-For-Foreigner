package com.example.project.reply.service;

import com.example.project.board.domain.Board;
import com.example.project.reply.domain.Reply;
import com.example.project.reply.dto.ReplyRequestDto;
import com.example.project.board.repository.BoardRepository;
import com.example.project.reply.repository.ReplyRepository;
import com.example.project.member.domain.Member;
import com.example.project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Override
    @Transactional
    public Long writeReply(ReplyRequestDto replyRequestDto, Long boardId, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(()
                -> new UsernameNotFoundException("존재하지 않는 사용자."));
        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("게시물을 찾을 수 없습니다."));
//        replyRequestDto.setMember(member);
//        replyRequestDto.setBoard(board);

        Reply reply = Reply.builder()
                // 추가
                .writer(replyRequestDto.getWriter())
                .content(replyRequestDto.getContent())
                .member(member)
                .board(board)
                .build();
        replyRepository.save(reply);

        // 댓글 ID
        return reply.getReplyId();
    }
}
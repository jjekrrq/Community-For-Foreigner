package com.example.project.reply.service;

import com.example.project.reply.dto.ReplyRequestDto;
import com.example.project.reply.dto.ReplyResponseDto;

import java.util.List;

public interface ReplyService {
    /*
    * 댓글 작성
    * @param replyRequestDto 댓글 정보
    * @param boardId 게시물
    * @param email 작성자
    * @return 댓글 ID
    * */
    Long writeReply(ReplyRequestDto replyRequestDto, Long boardId, String email);
    List<ReplyResponseDto> getReplyLists(Long boardId);
    ReplyResponseDto updateReply(Long replyId, ReplyRequestDto replyRequestDto);
    void deleteReplyById(Long replyId);
}

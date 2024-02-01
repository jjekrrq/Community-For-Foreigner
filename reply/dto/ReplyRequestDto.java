package com.example.project.reply.dto;
// id, title, content, writer, board(게시글에 대한 정보를 주면 이를 활용할 수 있음

import com.example.project.board.domain.Board;
import com.example.project.member.domain.Member;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ReplyRequestDto { // 이승창
    private Long replyId;
    private String content;
    private Member member;
    private Board board;
//    private Member member; // 현재 로그인이 되어 있는 상태라 작성자를 어떻게 끌어오지
//    private Board board;
}

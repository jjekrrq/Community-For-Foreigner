package com.example.project.reply.dto;

import com.example.project.reply.domain.Reply;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ReplyResponseDto { // 이승창
    private Long replyId;
    private String contents;
    private String writer;

    public static ReplyResponseDto of(Reply reply){
       return ReplyResponseDto.builder()
               .replyId(reply.getReplyId())
               .contents(reply.getContent())
               .writer(reply.getWriter())
               .build();
    }
}

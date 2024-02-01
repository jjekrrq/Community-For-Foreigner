package com.example.project.reply.domain;

import com.example.project.board.domain.Board;
import com.example.project.member.domain.Member;
import com.example.project.reply.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) //프록시 객체를 위한. 지연 로딩.
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Reply { // 이승창
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPLY_ID")
    private Long replyId;

    // 댓글 내용
    @Column(nullable = false)
    private String content;

    // 작성자. / Member 클래스와 N:1 연관관계 맵핑한 것으로 작성자 주입.
    private String writer;

    // 유저 한 명이 여러 개의 댓글을 달 수 있음.
    // Member클래스와 Reply클래스 사이의 연관 관계 맵핑 가능/ 한 명의 사람이 여러 개의 댓글을 달 수 있음.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Member member;

    // 게시글 하나에 여러 개의 댓글들이 달릴 수 있음. 즉, '다'에 해당함으로 ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    // 댓글 업데이트
    public void update(ReplyRequestDto replyRequestDto){
        if(replyRequestDto.getContent() != null){
            this.content = replyRequestDto.getContent();;
        }
    }
}

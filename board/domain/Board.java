package com.example.project.board.domain;

import com.example.project.board.dto.BoardRequestDto;
import com.example.project.member.domain.Member;
import com.example.project.reply.domain.Reply;
import com.example.project.time.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity// 기본 생성자
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;
    //추가
    @Column(length = 20, nullable = false) // 크기 20
    private String writer; // 글 작성자

    // 제목
    @Column(nullable = false)
    private String title;

    // 내용
    @Column(name = "BOARD_CONTENT", columnDefinition = "TEXT", nullable = false)
    private String contents;

    // 조회수
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    // 지역
    private String region;

    // 게시글을 작성하려면 작성자가 필요함. 작성자는 로그인 되어있는 계정.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Member member;

    // 이승창
    // 게시글 댓글. 양방향 연관 관계.
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Column(name = "BOARD_REPLY")
    @JsonIgnore
    @OrderBy("replyId desc ") // 댓글 작성시 최근 순으로
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    @Column(name = "BOARD_LIKE")
    @JsonIgnore
    private List<Hearts> hearts = new ArrayList<>();

    public void update(BoardRequestDto boardRequestDto) {
        if(boardRequestDto.getTitle() == null){
            this.contents = boardRequestDto.getContents();
            this.title = title;
        }
        if(boardRequestDto.getContents() == null){
            this.title = boardRequestDto.getTitle();
            this.contents = contents;
        }
        if((boardRequestDto.getContents() != null) && (boardRequestDto.getTitle() != null)){
            this.title = boardRequestDto.getTitle();
            this.contents = boardRequestDto.getContents();
        }
    }
    public void setMember(Member member){
        this.member = member;
    }
    public void removeReplies() {
        for (Reply reply : this.replies) {
            reply.setBoard(null);
        }
        this.replies.clear();
    }
}
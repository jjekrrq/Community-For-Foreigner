package com.example.project.board.domain;

import com.example.project.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class Hearts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 게시글을 여러개의 좋아요를 가질 수 있으므로 1:다 매핑
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 맴버는 여러개의 좋아요를 가질 수 있으므로 1:다 매핑
    @JoinColumn(name = "USER_ID", nullable = false)
    private Member member;

    public Hearts(Board board, Member member){
        this.board = board;
        this.member = member;
    }

    public static Hearts of(Board board) {
        return Hearts.builder()
                .board(board)
                .build();
    }
}

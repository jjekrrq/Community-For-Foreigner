package com.example.project.board.dto;

import com.example.project.member.domain.Member;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class BoardRequestDto {
    private Long boardId;
    private String contents;
    private String title;
    // 현재 로그인이 되어있는 상태이기 때문에 작성자의 정보는 필요없을 것 같음. / 없애야 할 것 같음.
//    private Member member; // 이새기 없애
//    private int heart;x
//    private int count;

    public BoardRequestDto(Long boardId, String title,String name, String contents){
        this.boardId = boardId;
//        this.writer = name;
        this.title = title;
        this.contents = contents;
//        this.heart = heart;
//        this.count = count;
    }
}

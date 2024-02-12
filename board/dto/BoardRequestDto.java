package com.example.project.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class BoardRequestDto {
    private Long boardId;
    private String contents;
    private String title;
    private String region;
}

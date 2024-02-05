package com.example.project.board.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class PageDto<T> {
    // 페이지 정보 : 페이지에 있는 게시글 개수라 보면 편함.
    private List<T> content;
    // 총 페이지 개수
    private int totalPages;
    // 총 게시글 개수
    private long totalElements;
    // 현재 페이지 번호
    private int nowPage;
    // 조회하는 페이지가 페이지 번호를 초과했을 때, 보여줄 에러 메세지
    private String errorMessage;

    public PageDto(List<T> content, int totalPages, long totalElements, int nowpage) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.nowPage = nowpage;
        this.errorMessage = null; // 초기에는 errorMessage를 null로 설정
    }
}

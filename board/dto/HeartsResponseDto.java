package com.example.project.board.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class HeartsResponseDto {
    private Long heartsCount;
}

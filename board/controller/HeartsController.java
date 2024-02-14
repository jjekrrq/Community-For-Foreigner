package com.example.project.board.controller;

import com.example.project.board.service.HeartsService;
import com.example.project.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Tag(name = "게시판 좋아요 API" , description = "게시판 좋아요 기능")
@RestController
@RequiredArgsConstructor
@RequestMapping("/hearts")
public class HeartsController {
    private final HeartsService heartsService;

    // CREATE : 좋아요 누르기
    @Operation(summary = "좋아요 누르기", description = "눌렀는지 안눌렀는지 확인 후 좋아요 누르기")
    @Parameter(name = "member", description = "현재 인증된 사용자 정보")
    @Parameter(name = "boardId", description = "좋아요를 입력할 게시글 ID")
    @PostMapping("/{boardId}")
    public ResponseEntity<String> addHearts(@AuthenticationPrincipal Member member, @PathVariable Long boardId){
        boolean result = false;

        if(member != null){
            result = heartsService.addHearts(member, boardId);
        }
        return result ?
                new ResponseEntity<>("좋아요 추가", HttpStatus.OK)
                :new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }

    // DELETE : 좋아요 삭제하기
    @Operation(summary = "좋아요 삭제하기", description = "눌렀는지 안눌렀는지 확인 후 좋아요 삭제하기")
    @Parameter(name = "member", description = "현재 인증된 사용자 정보")
    @Parameter(name = "boardId", description = "좋아요를 삭제할 게시글 ID")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteHearts(@AuthenticationPrincipal Member member, @PathVariable Long boardId){
        boolean result = false;

        if(member != null){
            result = heartsService.deleteHearts(member, boardId);
        }
        return result ?
                new ResponseEntity<>("좋아요 삭제", HttpStatus.OK)
                :new ResponseEntity<>("error", HttpStatus.BAD_REQUEST);
    }
}

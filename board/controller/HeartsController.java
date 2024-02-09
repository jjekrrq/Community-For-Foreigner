package com.example.project.board.controller;

import com.example.project.board.service.HeartsService;
import com.example.project.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hearts")
public class HeartsController {
    private final HeartsService heartsService;

    // CREATE : 좋아요 누르기
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

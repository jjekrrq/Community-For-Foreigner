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

    @PostMapping("/{boardId}")
    public ResponseEntity<String> addHearts(@AuthenticationPrincipal Member member, @PathVariable Long boardId){
        boolean result = false;

        if(member != null){
            result = heartsService.addHearts(member, boardId);
        }
        return result ?
                new ResponseEntity<>(HttpStatus.OK)
                :new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteHearts(@AuthenticationPrincipal Member member, @PathVariable Long boardId){

    }
}

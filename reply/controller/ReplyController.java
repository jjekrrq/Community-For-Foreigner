package com.example.project.reply.controller;

import com.example.project.member.domain.Member;
import com.example.project.reply.dto.ReplyRequestDto;
import com.example.project.reply.service.ReplyService;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
@RestController
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    /*
    * 댓글 작성
    * @param id 게시물
    * @param replyRequestDto 댓글 정보
    * @param authentication 유저 정보
    * @return 게시물 상세 페이지
    * */
    @PostMapping("/board/{id}/  reply")
    public ResponseEntity<String> writeReply(@PathVariable Long boardId, @RequestBody ReplyRequestDto replyRequestDto,
                                     @AuthenticationPrincipal Member member) {

//         UserDetails 클래스로부터 사용자 정보를 getPrincipal()메소드를 활용해 가져옴.
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        System.out.println(userDetails);
        String email = member.getEmail();
        replyService.writeReply(replyRequestDto, boardId, email);
        // 게시글 상세 페이지
//        return "redirect:/board/" + boardId;
        return new ResponseEntity<>("글 작성", HttpStatus.OK);
    }
}
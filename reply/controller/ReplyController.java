package com.example.project.reply.controller;

import com.example.project.member.domain.Member;
import com.example.project.reply.dto.ReplyRequestDto;
import com.example.project.reply.dto.ReplyResponseDto;
import com.example.project.reply.service.ReplyService;
import com.google.firebase.auth.FirebaseToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "댓글 API", description = "Swagger 댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {
    private final ReplyService replyService;
    /*
    * 댓글 작성
    * @param id 게시물
    * @param replyRequestDto 댓글 정보
    * @param authentication 유저 정보
    * @return 게시물 상세 페이지
    * */ 
    // CREATE : 댓글 작성하기 / 구현 완료
    @Operation(summary = "댓글 작성", description = "게시글의 ID를 URL에 받아서 댓글을 작성 ")
    @Parameter(name = "boardID", description = "게시글의 ID(PK값)")
    @Parameter(name = "replyRequestDto", description = "게시글에 작성될 내용. 즉, 데이터베이스에 저장할 내용")
    @Parameter(name = "member", description = "현재 로그인 되어 있는 사용자의 정보를 담고 있는 객체")
    @PostMapping("/{boardId}/create")
    public ResponseEntity<String> writeReply(@PathVariable("boardId") Long boardId, @RequestBody ReplyRequestDto replyRequestDto,
                                     @AuthenticationPrincipal Member member) {

        String email = member.getEmail();
        replyService.writeReply(replyRequestDto, boardId, email);

        // 게시글 상세 페이지
//        return "redirect:/board/details/" + boardId;
//        return new ResponseEntity<>("댓글 작성", HttpStatus.OK);
        return ResponseEntity.ok("redirect:/board/details/" + boardId);
    }
    // READ : 게시글의 전체 댓글 불러오기 / 구현 완료
    @Operation(summary = "댓글 조회", description = "게시글에 달려있는 댓글 조회")
    @Parameter(name = "boardId", description = "게시글에 달려있는 댓글을 조회할 것이기 때문에 게시글을 찾기 위한 식별자가 필요함")
    @GetMapping("/{boardId}/list")
    public ResponseEntity<List<ReplyResponseDto>> getReplies(@PathVariable("boardId") Long boardId){
        return new ResponseEntity<>(replyService.getReplyLists(boardId), HttpStatus.OK);
    }

    // UPDATE : 댓글 수정 / 구현 완료
    @Operation(summary = "댓글 수정", description = "댓글을 찾아 수정")
    @Parameter(name = "replyId", description = "댓글을 수정할 것이기 때문에 댓글의 고유 번호를 받아 수정함.")
    @Parameter(name = "replyRequestDto", description = "수정할 내용을 담은 객체")
    @PutMapping("/{replyId}/update")
    public ResponseEntity<ReplyResponseDto> updateReply(@PathVariable("replyId") Long replyId, @RequestBody ReplyRequestDto replyRequestDto){
        ReplyResponseDto replyResponseDto = replyService.updateReply(replyId, replyRequestDto);
        return ResponseEntity.ok(replyResponseDto);
    }
    // DELETE : 댓글 삭제 / 구현 완료
    @Operation(summary = "댓글 삭제", description = "댓글을 찾아 삭제")
    @Parameter(name = "replyId", description = "댓글을 조회해 삭제하기 위해 댓글의 고유 식별자(PK)를 인자로 넘겨줌.")
    @DeleteMapping("/{replyId}/delete")
    public ResponseEntity<String> deleteReply(@PathVariable("replyId") Long replyId){
        replyService.deleteReplyById(replyId);
        return new ResponseEntity<>("댓글 삭제", HttpStatus.OK);
    }
}
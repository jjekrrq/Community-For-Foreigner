package com.example.project.board.controller;

import com.example.project.board.dto.BoardRequestDto;
import com.example.project.board.dto.BoardResponseDto;
import com.example.project.board.dto.PageDto;
import com.example.project.board.service.BoardService;
import com.example.project.member.domain.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    /* Post 요청 이후 BoardRequestDto 객체 변환하여
           boardService.createBoard(boardDto);를 호출 하고 HTTP 상태 코드 200(OK) 반환
         */

    // CREATE : 구현 완료
    @PostMapping("/create")
    public ResponseEntity<String> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal Member member){

        Long userId = member.getId();
        boardService.createBoard(boardRequestDto, userId);
        return new ResponseEntity<>("글 작성", HttpStatus.OK);
//        return ResponseEntity.ok("redirect:/board/details/" + boardRequestDto.getBoardId());
    }
    // READ : 전체 불러오기. / 구현 완료.
    @GetMapping("/read")
    public ResponseEntity<List<BoardResponseDto>> readBoard(){
        return new ResponseEntity<>(boardService.readBoard(), HttpStatus.OK);
    }
    // READ : 전체 불러오기. / 페이징 처리
    // 페이지 시작은 0부터 시작함. 배열이라 보면 쉬움. ex) 1번 페이지 -> page = 0
    @GetMapping("/read/paging")
    public ResponseEntity<PageDto<BoardResponseDto>> readBoardWithPaging(@RequestParam(defaultValue = "6") int page, @RequestParam(defaultValue = "3") int size, Model model){
        Sort sort = Sort.by(Sort.Direction.DESC, "boardId");
        Pageable pageable = PageRequest.of(page, size, sort);
        return new ResponseEntity<>(boardService.readAllBoardWithPaging(pageable), HttpStatus.OK);
    }
    // READ : 유저 ID로 특정(유저 ID에 해당하는) 게시글 불러오기 / 구현 완료.
    @GetMapping("/read/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getSpecificBoard(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(boardService.getOneBoard(userId), HttpStatus.OK);
    }
    // READ : 게시글의 작성자로 게시글 조회하기 / 구현 완료.
    // URL 주소를 위와 달리 해야됨. read로 한다면, 충돌일어남.
    @GetMapping("/writer/{boardwriter}")
    public ResponseEntity<List<BoardResponseDto>> getBoardsThroughBoardWriter(@PathVariable("boardwriter") String boardWriter){
        return new ResponseEntity<>(boardService.getBoardsThroughWriter(boardWriter), HttpStatus.OK);
    }
    // READ : 상세 게시물 / 댓글도
    @GetMapping("/details/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardThroughBoardId(@PathVariable("boardId") Long boardId){
        return new ResponseEntity<>(boardService.getBoardThroughBoardId(boardId),HttpStatus.OK);
    }
    // UPDATE : 게시판 ID를 가지고 그에 해당하는 게시글 수정하기. / 구현 완료.
    @PatchMapping("/update/{boardId}") // 자원을 전체 교체하는 PutMapping보다 PatchMappin이 더 좋을 것 같다.
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("boardId") Long boardId,
                                                        @RequestBody BoardRequestDto boardRequestDto){
        BoardResponseDto updatedBoard = boardService.updateBoard(boardId, boardRequestDto);
        return ResponseEntity.ok(updatedBoard);
    }
    // DELETE : 구현 완료.
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>("게시글 삭제", HttpStatus.OK);
    }
}
package com.example.project.board.controller;

import com.example.project.board.dto.BoardRequestDto;
import com.example.project.board.dto.BoardResponseDto;
import com.example.project.board.dto.PageDto;
import com.example.project.board.service.BoardService;
import com.example.project.member.domain.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "게시판 API", description = "Swagger 게시판 API")
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
    @Operation(summary = "게시글 작성", description = "클라이언트에게 내용과 제목을 입력받아 데이터베이스에 저장")
    @Parameter(name = "boardRequestDto", description = "클라이언트에게 입력받는 부분")
    @Parameter(name = "member", description = "현재 로그인되어 있는 사용자를 받기 위한 Memeber객체")
    @PostMapping("/create")
    public ResponseEntity<String> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal Member member){

        Long userId = member.getId();
        boardService.createBoard(boardRequestDto, userId);
        return new ResponseEntity<>("글 작성", HttpStatus.OK);
//        return ResponseEntity.ok("redirect:/board/details/" + boardRequestDto.getBoardId());
    }
    // READ : 전체 불러오기. / 구현 완료.
    @Operation(summary = "전체 게시글 불러오기", description = "게시글 전체를 불러오기. 근데 페이징 처리 안됨.")
    @GetMapping("/read")
    public ResponseEntity<List<BoardResponseDto>> readBoard(){
        return new ResponseEntity<>(boardService.readBoard(), HttpStatus.OK);
    }
    // READ : 전체 불러오기. / 페이징 처리
    // 페이지 시작은 0부터 시작함. 배열이라 보면 쉬움. ex) 1번 페이지 -> page = 0
    @Operation(summary = "페이지단위로 분리된 게시글 불러오기", description = "페이지 번호를 URL로 받아 해당 페이지 반환")
    @Parameter(name = "page", description = "조회하고자 하는 페이지 번호")
    @Parameter(name = "size", description = "한 페이지 안에 들어갈 수 있는 게시글 수")
    @Parameter(name = "model", description = "서버에서 클라이언트로 데이터를 전달해주는 객체인데 사용X")
    @GetMapping("/read/paging")
    public ResponseEntity<PageDto<BoardResponseDto>> readBoardWithPaging(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, Model model){
        Sort sort = Sort.by(Sort.Direction.DESC, "boardId");
        Pageable pageable = PageRequest.of(page, size, sort);
        return new ResponseEntity<>(boardService.readAllBoardWithPaging(pageable), HttpStatus.OK);
    }
    // READ : 유저 ID로 특정(유저 ID에 해당하는) 게시글 불러오기 / 구현 완료.
    @Operation(summary = "유저가 작성한 게시글 불러오기", description = "유저의 PK(고유값)을 기준으로 그에 해당하는 게시글 불러오기")
    @Parameter(name = "userId", description = "URL에 달려있는 유저 ID값을 @PathVariable로 받아 그에 해당하는 정보를 반환하기 위한 매개변수")
    @GetMapping("/read/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getSpecificBoard(@PathVariable("userId") Long userId){
        return new ResponseEntity<>(boardService.getOneBoard(userId), HttpStatus.OK);
    }
    // READ : 게시글의 작성자로 게시글 조회하기 / 구현 완료.
    // URL 주소를 위와 달리 해야됨. read로 한다면, 충돌일어남.
    @Operation(summary = "작성자 이름으로 게시글 불러오기", description = "작성자 이름을 URL에 받아 그에 해당하는 게시글들 불러오기")
    @Parameter(name = "boardWriter", description = "URL에 달려있는 게시글 작성자 이름을 받는 매개변수")
    @GetMapping("/writer/{boardwriter}")
    public ResponseEntity<List<BoardResponseDto>> getBoardsThroughBoardWriter(@PathVariable("boardwriter") String boardWriter){
        return new ResponseEntity<>(boardService.getBoardsThroughWriter(boardWriter), HttpStatus.OK);
    }
    // READ : 상세 게시물 / 댓글도
    @Operation(summary = "게시글 상세 조회", description = "게시글 고유 ID를 가지고 해당 게시글을 상세 조회하는 API")
    @Parameter(name = "boardId", description = "게시글의 고유 ID(PK)")
    @GetMapping("/details/{boardId}")
    public ResponseEntity<BoardResponseDto> getBoardThroughBoardId(@PathVariable("boardId") Long boardId){
        return new ResponseEntity<>(boardService.getBoardThroughBoardId(boardId),HttpStatus.OK);
    }
    // UPDATE : 게시판 ID를 가지고 그에 해당하는 게시글 수정하기. / 구현 완료.
    @Operation(summary = "게시글 수정", description = "게시글 ID를 가지고 게시글을 수정하는 API")
    @Parameter(name = "boardId", description = "게시글의 고유 ID(PK)")
    @Parameter(name = "boardRequestDto", description = "수정할 내용을 담고 있는 변수 / 클라이언트에게 받는다.")
    @PatchMapping("/update/{boardId}") // 자원을 전체 교체하는 PutMapping보다 PatchMappin이 더 좋을 것 같다.
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable("boardId") Long boardId,
                                                        @RequestBody BoardRequestDto boardRequestDto){
        BoardResponseDto updatedBoard = boardService.updateBoard(boardId, boardRequestDto);
        return ResponseEntity.ok(updatedBoard);
    }
    // DELETE : 구현 완료.

    @DeleteMapping("/delete/{boardId}")
    @Operation(summary = "게시글 삭제", description = "게시글 고유 ID를 가지고 게시글을 삭제하는 API / 게시글이 삭제되면 해당 게시글에 달린 댓글도 자동 삭제")
    @Parameter(name = "boardId", description = "게시글의 고유 ID(PK)")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") Long boardId){
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>("게시글 삭제", HttpStatus.OK);
    }
}
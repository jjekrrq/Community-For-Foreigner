package com.example.project.member.controller;

import com.example.project.member.domain.Member;
import com.example.project.member.dto.UserInfo;
import com.example.project.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {
    private MemberService memberService;
    // 되는 거 확인했다.
    // 프론트에서 로그인 된 유저 ID or Email을 넘겨줄 수 있나?
    // 이거만 되면 ㄹㅇ 할 수 있을 것 같은데
    @GetMapping("/user")
    public String userName(@AuthenticationPrincipal Member member){ // @AuthenticationalPrincipal = 객체를 인자로 받을 수 있게 해주는 어노테이션
        String userName = member.getEmail();
        return userName;
    }
    @GetMapping("/details")
    public UserInfo getUserInfo(@AuthenticationPrincipal Member member){
        return memberService.getUser(member);
    }

}
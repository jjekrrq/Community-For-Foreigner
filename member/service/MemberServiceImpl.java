package com.example.project.member.service;

import com.example.project.member.domain.Member;
import com.example.project.member.dto.UserInfo;
import com.example.project.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    @Override
    public UserInfo getUser(Member member){

        return UserInfo.builder()
                .id(member.getId())
                .email(member.getEmail())
                .locale(member.getLocal())
                .name(member.getName())
                .build();
    }

}

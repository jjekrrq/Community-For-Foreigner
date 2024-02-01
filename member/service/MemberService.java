package com.example.project.member.service;

import com.example.project.member.domain.Member;
import com.example.project.member.dto.UserInfo;

public interface MemberService {
    UserInfo getUser(Member member);
}

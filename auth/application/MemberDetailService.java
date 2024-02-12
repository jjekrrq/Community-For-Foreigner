package com.example.project.auth.application;


import com.example.project.auth.exception.MemberNotFoundException;
import com.example.project.member.domain.Member;
import com.example.project.member.domain.Role;
import com.example.project.member.dto.UserInfo;
import com.example.project.member.repository.MemberRepository;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(FirebaseToken firebaseToken, Role role) {
        Member member = Member.builder()
                .memberName(firebaseToken.getUid())
                .email(firebaseToken.getEmail())
                .name(firebaseToken.getName())
                .picture(firebaseToken.getPicture())
                .role(role)
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    public Member updateByUsername(FirebaseToken firebaseToken) {
        Member member = memberRepository.findByMemberName(firebaseToken.getUid())
                .orElseThrow(() -> new MemberNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", firebaseToken.getName())));

        member.update(firebaseToken);

        return memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberName(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("해당 유저(%s)를 찾을 수 없습니다.", username)));
    }
    @Transactional
    public UserInfo loadUserByUserId(Long userId){
        Member member = memberRepository.findById(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없음"));
        return UserInfo.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}

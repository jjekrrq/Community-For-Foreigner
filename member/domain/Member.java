package com.example.project.member.domain;

import com.example.project.board.domain.Board;
import com.example.project.reply.domain.Reply;
import com.google.firebase.auth.FirebaseToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails { // 구글 로그인은 OAuth2User를 implements 받아야 한다.
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberName;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    @Column(name = "USER_EMAIL", nullable = false)
    private String email;

    @Column(name = "USER_LOCAL")
    private String local; // 지역

    @Column(name = "USER_GENDER")
    private String gender; // 성별

    private String picture;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    // 이승창
    // 유저가 단 댓글 찾기
    @OneToMany(mappedBy = "member")
    private List<Reply> replies = new ArrayList<>();
//    @OneToMany(mappedBy = "member")
//    @Column(name = "USER_REPLY")
//    private List<Reply> replies = new ArrayList<>();

    // 이승창 / 작성해야 됨.
    // 유저가 작성한 게시글 찾기

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Builder
    private Member(String memberName, Role role, String email, String name, String picture) {
        this.memberName = memberName;
        this.role = role;
        this.email = email;
        this.name = name;
    }

    public void update(FirebaseToken token) {
        this.memberName = token.getUid();
        this.email = token.getEmail();
        this.name = token.getName();
        this.picture = token.getPicture();
    }

}

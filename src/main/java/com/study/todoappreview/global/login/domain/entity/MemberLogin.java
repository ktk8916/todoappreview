package com.study.todoappreview.global.login.domain.entity;

import com.study.todoappreview.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberLogin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;

    public static MemberLogin from(Member member) {
        MemberLogin memberLogin = new MemberLogin();
        memberLogin.member = member;
        memberLogin.createdAt = LocalDateTime.now();
        memberLogin.endedAt = LocalDateTime.now().plusMinutes(10);
        return memberLogin;
    }
}

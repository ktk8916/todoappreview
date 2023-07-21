package com.study.todoappreview.global.login.service;

import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final MemberLoginRepository memberLoginRepository;
    public void insert(Member member){
        MemberLogin memberLogin = MemberLogin.from(member);
        memberLoginRepository.save(memberLogin);
    }

    public MemberLogin findByMemberLastLoginLog(Long memberId){
        return memberLoginRepository
                .findFirstByMemberIdOrderByEndedAtDesc(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member isValidLogin(Long memberId){
        MemberLogin memberLogin = findByMemberLastLoginLog(memberId);
        if(LocalDateTime.now().isAfter(memberLogin.getEndedAt())){
            throw new RuntimeException("로그인 시간 초과");
        } else {
            return memberLogin.getMember();
        }
    }
}

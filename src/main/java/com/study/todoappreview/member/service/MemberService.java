package com.study.todoappreview.member.service;

import com.study.todoappreview.global.auth.AuthService;
import com.study.todoappreview.global.login.service.MemberLoginService;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.domain.response.MemberResponse;
import com.study.todoappreview.member.exception.DuplicateEmailException;
import com.study.todoappreview.member.exception.LoginFailException;
import com.study.todoappreview.member.repository.MemberRepository;
import com.study.todoappreview.member.domain.request.LoginRequest;
import com.study.todoappreview.member.domain.request.SignupRequest;
import com.study.todoappreview.member.domain.response.LoginResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberLoginService memberLoginService;
    private final AuthService authService;

    public Page<MemberResponse> findAll(PageRequest pageRequest){
        Page<Member> members = memberRepository.findAllBy(pageRequest);
        return members.map(MemberResponse::from);
    }

    public Map<String, Object> getTokenToData(String token){
        return authService.getClaims(token);
    }

    public void signup(SignupRequest signupRequest){
        Optional<Member> byEmail = memberRepository.findByEmail(signupRequest.email());

        if(byEmail.isPresent()){
            throw new DuplicateEmailException();
        }

        memberRepository.save(signupRequest.toEntity());
    }

    public LoginResponse login(LoginRequest loginRequest){
        Member member = memberRepository
                .findByEmailAndPassword(
                        loginRequest.email(),
                        loginRequest.password())
                .orElseThrow(LoginFailException::new);

        memberLoginService.insert(member);

        String token = authService.makeToken(member);

        return LoginResponse.of(member, token);
    }
}

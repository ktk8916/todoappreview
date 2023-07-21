package com.study.todoappreview.member.domain.service;

import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.domain.request.LoginRequest;
import com.study.todoappreview.member.domain.request.SignupRequest;
import com.study.todoappreview.member.exception.DuplicateEmailException;
import com.study.todoappreview.member.exception.MemberNotFoundException;
import com.study.todoappreview.member.repository.MemberRepository;
import com.study.todoappreview.member.domain.response.LoginResponse;
import com.study.todoappreview.member.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberLoginRepository memberLoginRepository;

    String email = "aaa";
    String password = "1234";
    String name = "memberA";
    Integer age = 11;

    @BeforeEach
    void init(){

        Member member = new Member(null, email, password, name, age, null, null);
        memberRepository.save(member);
    }

    @AfterEach
    void clear(){
        memberRepository.deleteAll();
        memberLoginRepository.deleteAll();
    }

    @Test
    void 기본로그인() {
        //given
        LoginRequest loginRequest = new LoginRequest(email, password);

        //when
        LoginResponse login = memberService.login(loginRequest);

        //then
        assertThat(login.id()).isEqualTo(1L);
        assertThat(login.name()).isEqualTo(name);
        assertThat(login.age()).isEqualTo(age);
    }

    @Test
    void 기본로그인_멤버로그인_인서트_체크() {

        //given
        Member member = new Member(null, email, password, name, age, null, null);
        memberRepository.save(member);
        LoginRequest loginRequest = new LoginRequest(email, password);

        //when
        LoginResponse loginResponse = memberService.login(loginRequest);
        List<MemberLogin> logins = memberLoginRepository.findAll();
        MemberLogin login = logins.get(0);

        //then
        assertThat(loginResponse.id()).isNotNull();
        assertThat(loginResponse.name()).isEqualTo(name);
        assertThat(loginResponse.age()).isEqualTo(age);

        assertThat(logins).hasSize(1);
        assertThat(login.getMember()).isEqualTo(member);
        assertThat(login.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(login.getEndedAt()).isAfter(LocalDateTime.now());
    }

    @Test
    void 로그인시_없는유저(){
        //given
        String wrongEmail = "누가봐도 틀린 이메일";
        String wrongPassword = "누가봐도 틀린 비밀번호";

        LoginRequest loginRequest = new LoginRequest(wrongEmail, wrongPassword);

        //when
        MemberNotFoundException memberNotFoundException =
                assertThrows(
                        MemberNotFoundException.class,
                        () -> memberService.login(loginRequest));

        //then
        assertThat(memberNotFoundException).hasMessage("MEMBER NOT FOUND");
    }

    @Test
    void 새로운_멤버_회원가입시_성공(){
        //given
        String newEmail = email+"new";
        SignupRequest signupRequest = new SignupRequest(newEmail, password, name, age);

        //when
        memberService.signup(signupRequest);
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).hasSize(2);
    }

    @Test
    void 중복된_이메일_회원가입시_예외(){
        //given
        String sameEmail = email;
        SignupRequest signupRequest = new SignupRequest(sameEmail, password, name, age);

        //when, then
        assertThrows(DuplicateEmailException.class, ()->memberService.signup(signupRequest));

    }
}
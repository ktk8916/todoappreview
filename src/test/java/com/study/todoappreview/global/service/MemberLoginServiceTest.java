package com.study.todoappreview.global.service;

import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.global.login.service.MemberLoginService;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberLoginServiceTest {
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberLoginRepository memberLoginRepository;
//    @Autowired
//    MemberLoginService memberLoginService;
//
//    String email = "aaa";
//    String password = "1234";
//    String name = "memberA";
//    Integer age = 11;
//    Member member;
//
//    @BeforeEach
//    void init(){
//
//        Member member = new Member(null, email, password, name, age, null, null);
//        this.member = memberRepository.save(member);
//
//        memberLoginRepository.save(MemberLogin.from(member));
//    }
//
//    @AfterEach
//    void clear(){
//        memberLoginRepository.deleteAll();
//        memberRepository.deleteAll();
//    }
//
//
//    @Test
//    void insert() {
//    }
//
//    @Test
//    void findByMember() {
//        //given
//        Long memberId = this.member.getId();
//
//        //when
//        Member member = memberLoginService.findByMember(memberId);
//
//        //then
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    void 가장_최근것_찾기(){
//        //given
//        Member member = memberRepository.findAll().get(0);
//        Long memberId = member.getId();
//        MemberLogin memberLogin = MemberLogin.from(member);
//
//        //when
//        MemberLogin save = memberLoginRepository.save(memberLogin);
//        Member byMember = memberLoginService.findByMember(memberId);
//
//        //when
//        assertThat(save.getMember()).isEqualTo(byMember);
//    }
//
//    @Test
//    void 로그인_상태가_아닌_경우(){
//        //given
//        Long wrongMemberId = 200L;
//
//        //when
//        RuntimeException runtimeException = assertThrows(
//                RuntimeException.class,
//                () -> memberLoginService.findByMember(wrongMemberId));
//
//        //then
//        assertThat(runtimeException).hasMessage("로그인 상태가 아닙니다");
//    }
}
package com.study.todoappreview.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.repository.MemberRepository;
import com.study.todoappreview.member.service.MemberService;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.repository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EntityManager em;

//    @MockBean
//    MemberService memberService;

    @Test
    void 멤버_전체_조회() throws Exception {
        //given


        //when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/member"));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].todos[0].title").value("첫번째글제목"));
    }

//    @Test
//    void 로그인_성공() throws Exception {
//        //given
//        LoginRequest loginRequest = new LoginRequest(email, password);
//        Mockito
//                .when(memberService.login(loginRequest))
//                .thenReturn(new LoginResponse(1L, "memberA", 11));
//
//        //when
//        ResultActions perform = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/api/v1/member/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)));
//
//        //then
//        perform.andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.name").value("memberA"))
//                .andExpect(jsonPath("$.age").value(11));
//    }
//
//    @Test
//    void 로그인_실패() throws Exception {
//        //given
//        String wrongEmail = "누가봐도 틀린 이메일";
//        LoginRequest loginRequest = new LoginRequest(wrongEmail, password);
//        Mockito
//                .doThrow(new LoginFailException())
//                .when(memberService)
//                .login(loginRequest);
//
//        //when
//        ResultActions perform = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/api/v1/member/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)));
//
//        //then
//        perform.andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    void 회원가입_성공() throws Exception {
//        //given
//        String newEmail = email + "new";
//        SignupRequest signupRequest = new SignupRequest(newEmail, password, name, age);
//        Mockito
//                .doNothing()
//                .when(memberService)
//                .signup(signupRequest);
//
//        //when
//        ResultActions perform = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/api/v1/member/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(signupRequest)));
//
//        //then
//        perform.andExpect(status().isCreated());
//    }
//
//    @Test
//    void 회원가입_실패() throws Exception {
//        //given
//        String sameEmail = email;
//        SignupRequest signupRequest = new SignupRequest(sameEmail, password, name, age);
//        Mockito
//                .doThrow(DuplicateEmailException.class)
//                .when(memberService)
//                .signup(signupRequest);
//
//        //when
//        ResultActions perform = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .post("/api/v1/member/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(signupRequest)));
//
//        //then
//        perform.andExpect(status().isBadRequest());
//    }
//



    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    MemberLoginRepository memberLoginRepository;

    String email = "aaa";
    String password = "1234";
    String name = "memberA";
    Integer age = 11;
    Member member;

    @BeforeEach
    void init(){

        Member member = new Member(null, email, password, name, age, new ArrayList<>(), null);
        this.member = memberRepository.save(member);

        memberLoginRepository.save(MemberLogin.from(member));
        
        todoRepository.save(
                Todo.builder()
                        .title("첫번째글제목")
                        .content("첫번째글내용")
                        .isDone(false)
                        .likeCount(0)
                        .member(this.member)
                        .build()
        );

        todoRepository.save(
                Todo.builder()
                        .title("두번째글제목")
                        .content("두번째글내용")
                        .isDone(false)
                        .likeCount(0)
                        .member(this.member)
                        .build()
        );

//        em.flush();
        em.clear();
    }

    @AfterEach
    void clear(){
        todoRepository.deleteAll();
        memberLoginRepository.deleteAll();
        memberRepository.deleteAll();
    }

}
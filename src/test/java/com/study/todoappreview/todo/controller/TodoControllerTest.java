package com.study.todoappreview.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.domain.request.LoginRequest;
import com.study.todoappreview.member.domain.response.LoginResponse;
import com.study.todoappreview.member.repository.MemberRepository;
import com.study.todoappreview.member.service.MemberService;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.repository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    Todo todo;
    @Nested
    class 투두_포스트_요청{
        @Test
        @DisplayName("성공")
        void insert() throws Exception{
            Map<String, Object> req = new HashMap<>();
            req.put("title", "ttt");
            req.put("content", "ccc");
            mockMvc.perform(
                            post("/api/v1/todo")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))
                                    .header("Authorization", token)
                    )
                    .andExpect(
                            status().isCreated());
            List<Todo> all = todoRepository.findAll();
            assertEquals(all.size(), 42);
        }
        @Test
        @DisplayName("토큰 실패")
        void insertFail() throws Exception{
            Map<String, Object> req = new HashMap<>();
            req.put("title", "ttt");
            req.put("content", "ccc");
            mockMvc.perform(
                            post("/api/v1/todo")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))
                    )
                    .andExpect(
                            status().isBadRequest());

            List<Todo> all = todoRepository.findAll();
            assertEquals(all.size(), 41);
        }
    }
    @Nested
    class 투두_체크하기{
        @Test
        @DisplayName("성공")
        void insert() throws Exception{
            mockMvc.perform(
                            put("/api/v1/todo/" + todo.getId() + "/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("Authorization", token)
                    )
                    .andExpect(
                            status().isAccepted());
            Optional<Todo> byId = todoRepository.findById(todo.getId());
            assertTrue(byId.get().isDone());
        }
        @Test
        @DisplayName("토큰 실패")
        void insertFail() throws Exception{
            Map<String, Object> req = new HashMap<>();
            req.put("title", "ttt");
            req.put("content", "ccc");
            mockMvc.perform(
                            put("/api/v1/todo/" + todo.getId() + "/check")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(req))
                    )
                    .andExpect(
                            status().isBadRequest());
            Optional<Todo> byId = todoRepository.findById(todo.getId());
            assertFalse(byId.get().isDone());
        }
    }
    @Nested
    class 투두_가져오기{
        @Test
        @DisplayName("그냥 가져오기")
        void getDefault() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(20)))
                    .andExpect(
                            jsonPath("$.content.[0].title")
                                    .value("a"))
                    .andExpect(
                            jsonPath("$.content.[0].content")
                                    .value("a"))
                    .andExpect(jsonPath("$.totalElements")
                            .value(41))
            ;
        }
        @Test
        @DisplayName("타이틀로 가져오기")
        void likeTitle() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("title", "t")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(1)))
                    .andExpect(
                            jsonPath("$.content.[0].id").isNotEmpty())
                    .andExpect(
                            jsonPath("$.content.[0].title")
                                    .value("t"))
                    .andExpect(
                            jsonPath("$.content.[0].content")
                                    .value("t"))
                    .andExpect(jsonPath("$.totalElements")
                            .value(1))
            ;
        }

        @Test
        @DisplayName("체크한거")
        void isDoneTrue() throws Exception{
            Todo todo1 = new Todo(todo.getId(), todo.getTitle(), todo.getContent(), true, todo.getLikeCount(), todo.getMember());
            todoRepository.save(todo1);
            entityManager.flush();
            entityManager.clear();
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("isDone", "true")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(1)))
                    .andExpect(
                            jsonPath("$.content.[0].title")
                                    .value("a"))
                    .andExpect(jsonPath("$.totalElements")
                            .value(1))
            ;
        }

        @Test
        @DisplayName("타이틀 이랑 CONTENT 둘다 있는것")
        void existTitleAndContent() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("title", "t2")
                                    .param("content", "t")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(1)))
                    .andExpect(
                            jsonPath("$.content.[0].title")
                                    .value("t2"))
                    .andExpect(
                            jsonPath("$.content.[0].content")
                                    .value("t2"))
                    .andExpect(jsonPath("$.totalElements")
                            .value(1))
            ;
        }

        @Test
        @DisplayName("CONTENT 만 있는 것")
        void existContent() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("content", "t")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(20)))
                    .andExpect(jsonPath("$.totalElements")
                            .value(40))
            ;
        }

        @Test
        @DisplayName("goe")
        void goe() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("likeGoe", "30")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(10)))
                    .andExpect(jsonPath("$.totalElements")
                            .value(10))
            ;
        }
        @Test
        @DisplayName("loe")
        void loe() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("likeLoe", "5")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(6)))
                    .andExpect(jsonPath("$.totalElements")
                            .value(6))
            ;
        }
        @Test
        @DisplayName("loeAndGoe")
        void loeAndGoe() throws Exception{
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("likeGoe", "10")
                                    .param("likeGoe", "15")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(6)))
                    .andExpect(jsonPath("$.totalElements")
                            .value(6))
            ;
        }
    }


    @Test
        @DisplayName("체크한거")
        void isDoneTure() throws Exception{
            Todo todo1 = new Todo(todo.getId(), todo.getTitle(), todo.getContent(), true, todo.getLikeCount(), todo.getMember());
            todoRepository.save(todo1);
            entityManager.flush();
            entityManager.clear();
            mockMvc.perform(
                            get("/api/v1/todo")
                                    .param("isDone", "true")
                    )
                    .andExpect(
                            status().isOk())
                    .andExpect(
                            jsonPath("$.content", hasSize(1)))
                    .andExpect(
                            jsonPath("$.content.[0].title").value(todo1.getTitle()))
            ;
        }




    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TodoRepository todoRepository;
    @Autowired
    MemberLoginRepository memberLoginRepository;
    String email = "1111";
    String password = "1234";
    Member member;
    @Autowired
    EntityManager entityManager;
    @Autowired
    MemberService memberService;
    String token;
    @BeforeEach
    void init(){
        Member member =
                new Member(null, email, password
                        , "name", 10, new ArrayList<>(), null);

        this.member = memberRepository.save(member);
        this.todo = todoRepository.save(
                new Todo(null, "a", "a"
                        , false, 0, member)
        );
        for (int i = 0; i < 40; i++) {
            todoRepository.save(
                    new Todo(null, "t" + i,"t" + i
                            , false, i, member)
            );
        }

        MemberLogin entity = MemberLogin.from(this.member);
        memberLoginRepository.save(entity);
        entityManager.flush();
        entityManager.clear();
        LoginResponse login = memberService.login(new LoginRequest(email, password));
        token = login.token();

    }
    @AfterEach
    void clean(){
        todoRepository.deleteAll();
        memberLoginRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
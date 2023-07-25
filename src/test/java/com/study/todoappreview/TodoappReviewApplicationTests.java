package com.study.todoappreview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.global.login.domain.entity.QMemberLogin;
import com.study.todoappreview.global.login.repository.MemberLoginRepository;
import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.member.domain.entity.QMember;
import com.study.todoappreview.member.domain.request.LoginRequest;
import com.study.todoappreview.member.domain.response.LoginResponse;
import com.study.todoappreview.member.repository.MemberRepository;
import com.study.todoappreview.member.service.MemberService;
import com.study.todoappreview.todo.domain.entity.QTodo;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.repository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.study.todoappreview.member.domain.entity.QMember.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TodoappReviewApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void test(){
        QMember member = new QMember("member");
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        String name = null;

        BooleanExpression nameEq = name != null ? member.name.eq("name") : null;
        BooleanExpression contains = member.name.contains("am");
        BooleanExpression age = member.age.goe(20);
        List<Member> members = jpaQueryFactory
                .select(member)
                .from(member)
                .where(contains, age)
                .fetch();
    }

    @Test
    void test2(){
        //select member
        // from Member member
        // where age <= 10 and age>5
        // and name = "na"

        QMember m = QMember.member;
        QMemberLogin ml = QMemberLogin.memberLogin;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);

        BooleanExpression ageGt = m.age.gt( 10);
        BooleanExpression ageLoe = m.age.loe(5);
        BooleanExpression notEqualName = m.name.ne("na");

        JPAQuery<Member> query = jpaQueryFactory
                .select(m)
                .from(m)
                .leftJoin(m.todos)
                .fetchJoin()
                .where(ageGt, ageLoe, notEqualName);

        int size = query.fetch().size();

        List<Member> members = query.fetch();
        for (Member member : members) {
            member.getTodos();
        }

        System.out.println();
    }

    @Test
    void test3(){
        QMember qm = QMember.member;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<String> query = jpaQueryFactory
                .select(
                        qm.name
                                .concat("님")
                                .concat(" ")
                                .concat(qm.age.stringValue()))
                .from(qm);
        List<String> membernames = query.fetch();
        System.out.println(membernames);
    }

    @Test
    void test4(){
        QMember qm = QMember.member;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        Member member1 = entityManager.find(Member.class, this.member.getId());
        Member member2 = memberRepository.findById(this.member.getId()).get();
        Member member3 = jpaQueryFactory
                .select(qm)
                .from(qm)
                .where(qm.id.eq(this.member.getId()))
                .fetchFirst();

        assertThat(member1).isEqualTo(member2).isEqualTo(member3).isNotEqualTo(this.member);
    }

    @Test
    void test5(){
        //작성자 이름이 name,
        //좋아요를 10개 이상 받고,
        //내용에 t가 들어간 todo

        QTodo qTodo = QTodo.todo;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<Todo> query = jpaQueryFactory
                .select(qTodo)
                .from(qTodo)
                .where(
                        qTodo.likeCount.goe(10),
                        qTodo.content.contains("t"),
                        qTodo.member.name.eq("name"));

        List<Todo> todos = query.fetch();
        assertThat(todos).hasSize(30);
    }

    @Test
    void test6(){
        QMember qMember = QMember.member;
        QTodo qTodo = QTodo.todo;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        JPAQuery<List<Todo>> query = jpaQueryFactory
                .select(qMember.todos)
                .from(qMember)
                .leftJoin(qMember.todos, qTodo)
                .fetchJoin()
                .where(
                        qMember.name.eq("name"),
                        qTodo.content.contains("t"),
                        qTodo.likeCount.goe(10));
    }

    @Test
    void test7(){
        QMember qMember = QMember.member;
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .where(qMember.in(
                        JPAExpressions
                                .select(QTodo.todo.member)
                                .from(QTodo.todo)
                                .where(QTodo.todo.content.contains("t"))));

        JPAQuery<String> query = jpaQueryFactory
                .select(
                        new CaseBuilder()
                                .when(qMember.age.between(0, 20))
                                .then("10대")
                                .otherwise("노인")
                );

    }



    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    Todo todo;


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

        // 이부분에 save 되는 좋아요 5개 이하인 todo가 7개인듯
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

package com.study.todoappreview.member.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.todoappreview.member.domain.dto.MemberCondition;
import com.study.todoappreview.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.study.todoappreview.member.domain.entity.QMember.member;

public class CustomMemberRepositoryImpl implements CustomMemberRepository{
    private final JPAQueryFactory queryFactory;

    public CustomMemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findAllByCondition(MemberCondition memberCondition, Pageable pageable) {
        JPAQuery<Member> query = queryFactory
                .select(member)
                .from(member)
                .leftJoin(member.todos)
                .where(
                        nameStartWith(memberCondition.lastName()),
                        ageGoe(memberCondition.ageGoe()),
                        ageLoe(memberCondition.ageLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Member> content = query.fetch();

        Long totalSize = queryFactory
                .select(member.count())
                .from(member)
                .leftJoin(member.todos)
                .where(
                        nameStartWith(memberCondition.lastName()),
                        ageGoe(memberCondition.ageGoe()),
                        ageLoe(memberCondition.ageLoe())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, totalSize);
    }

    private BooleanExpression nameStartWith(String lastName){
        return lastName==null ?
                null :
                member.name.startsWith(lastName);
    }

    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe==null ?
                null :
                member.age.goe(ageGoe);
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe==null ?
                null :
                member.age.loe(ageLoe);
    }


}

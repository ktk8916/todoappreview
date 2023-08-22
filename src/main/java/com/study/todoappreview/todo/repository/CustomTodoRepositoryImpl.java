package com.study.todoappreview.todo.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.todoappreview.member.domain.entity.QMember;
import com.study.todoappreview.todo.domain.dto.TodoCondition;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.domain.response.QTodoResponse;
import com.study.todoappreview.todo.domain.response.TodoResponse;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static com.study.todoappreview.todo.domain.entity.QTodo.todo;
import static com.study.todoappreview.member.domain.entity.QMember.member;

public class CustomTodoRepositoryImpl implements CustomTodoRepository{

    private final JPAQueryFactory queryFactory;
    private final QMember qMember = QMember.member;



    public CustomTodoRepositoryImpl(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<Todo> findAllByCondition(TodoCondition todoCondition, Pageable pageable){

        JPAQuery<Todo> query = queryFactory
                .select(todo)
                .from(todo)
                .innerJoin(todo.member, member)
                .fetchJoin()
                .where(
                        contentContains(todoCondition.content()),
                        titleEqual(todoCondition.title()),
                        isDoneEqual(todoCondition.isDone()),
                        likeCountLoe(todoCondition.likeLoe()),
                        likeCountGoe(todoCondition.likeGoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return query.fetch();
    }

    @Override
    public Page<TodoResponse> findAllByConditionToPage(TodoCondition todoCondition, Pageable pageable) {
        List<Todo> query = queryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.member)
                .fetchJoin()
                .where(
                        contentContains(todoCondition.content()),
                        titleEqual(todoCondition.title()),
                        isDoneEqual(todoCondition.isDone()),
                        likeCountLoe(todoCondition.likeLoe()),
                        likeCountGoe(todoCondition.likeGoe())
                ).fetch();
        List<TodoResponse> content = query.stream().map(TodoResponse::from).collect(Collectors.toList());

        Long totalSize = queryFactory
                .select(todo.count())
                .from(todo)
                .where(
                        contentContains(todoCondition.content()),
                        titleEqual(todoCondition.title()),
                        isDoneEqual(todoCondition.isDone()),
                        likeCountLoe(todoCondition.likeLoe()),
                        likeCountGoe(todoCondition.likeGoe())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, totalSize);
    }

    private BooleanExpression contentContains(String content) {
        return content == null ?
                null : todo.content.contains(content);
    }

    private BooleanExpression titleEqual(String title) {
        return title == null ?
                null : todo.content.eq(title);
    }

    private BooleanExpression isDoneEqual(Boolean isDone) {
        return isDone == null ?
                null : todo.isDone.eq(isDone);
    }
    private BooleanExpression likeCountLoe(Integer likeLoe) {
        return likeLoe == null ?
                null : todo.likeCount.loe(likeLoe);
    }
    private BooleanExpression likeCountGoe(Integer likeGoe) {
        return likeGoe == null ?
                null : todo.likeCount.goe(likeGoe);
    }


}


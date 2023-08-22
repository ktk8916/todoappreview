package com.study.todoappreview.todo.domain.response;

import com.querydsl.core.annotations.QueryProjection;
import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.todo.domain.entity.Todo;

public record TodoResponse(
        Long id,
        String title,
        String content,
        boolean isDone,
        Integer likeCount,
        MemberDto member
) {

    @QueryProjection
    public TodoResponse(Todo todo) {
        this(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.isDone(),
                todo.getLikeCount(),
                MemberDto.from(todo.getMember()));
    }

    public static TodoResponse from(Todo todo){
        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.isDone(),
                todo.getLikeCount(),
                MemberDto.from(todo.getMember())
        );
    }
}

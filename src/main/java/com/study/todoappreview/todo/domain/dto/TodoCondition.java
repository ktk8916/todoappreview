package com.study.todoappreview.todo.domain.dto;

import com.querydsl.core.annotations.QueryProjection;


public record TodoCondition(
        String title,
        String content,
        Boolean isDone,
        Integer likeLoe,
        Integer likeGoe
) {

}

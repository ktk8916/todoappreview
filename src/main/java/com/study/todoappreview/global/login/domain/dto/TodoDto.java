package com.study.todoappreview.global.login.domain.dto;

import com.study.todoappreview.todo.domain.entity.Todo;

public record TodoDto(Long id, String title, String content, boolean isDone, Integer likeCount) {

    public static TodoDto from(Todo todo){
        return new TodoDto(
                todo.getId(),
                todo.getTitle(),
                todo.getContent(),
                todo.isDone(),
                todo.getLikeCount()
        );
    }
}


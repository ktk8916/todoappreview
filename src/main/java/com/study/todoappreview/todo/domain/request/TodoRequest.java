package com.study.todoappreview.todo.domain.request;

import com.study.todoappreview.member.domain.entity.Member;
import com.study.todoappreview.todo.domain.entity.Todo;

public record TodoRequest(
        String title,
        String content) {

}

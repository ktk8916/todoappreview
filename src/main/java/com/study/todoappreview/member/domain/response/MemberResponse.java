package com.study.todoappreview.member.domain.response;

import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.global.login.domain.dto.TodoDto;
import com.study.todoappreview.member.domain.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

public record MemberResponse (MemberDto memberDto, List<TodoDto> todos) {

    public static MemberResponse from(Member member){
        return new MemberResponse(
                MemberDto.from(member),
                member.getTodos()
                        .stream()
                        .map(TodoDto::from)
                        .collect(Collectors.toList())
        );
    }
}

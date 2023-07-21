package com.study.todoappreview.member.domain.response;

import com.study.todoappreview.member.domain.entity.Member;

public record LoginResponse(
        Long id,
        String name,
        Integer age,
        String token) {

    public static LoginResponse of(Member member, String token){
        return new LoginResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                token
        );
    }
}

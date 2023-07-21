package com.study.todoappreview.global.login.domain.dto;

import com.study.todoappreview.member.domain.entity.Member;
public record MemberDto(Long id, String name, Integer age) {

    public static MemberDto from(Member member){
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getAge()
        );
    }
}


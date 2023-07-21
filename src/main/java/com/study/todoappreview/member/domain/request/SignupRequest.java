package com.study.todoappreview.member.domain.request;

import com.study.todoappreview.member.domain.entity.Member;

public record SignupRequest(
        String email,
        String password,
        String name,
        Integer age) {
    public Member toEntity(){
        return Member
                .builder()
                .email(email)
                .password(password)
                .age(age)
                .name(name)
                .build();
    }
}


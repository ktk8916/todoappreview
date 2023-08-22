package com.study.todoappreview.member.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

public record MemberCondition(String lastName, Integer ageLoe, Integer ageGoe) {

    @QueryProjection
    public MemberCondition(String lastName, Integer ageLoe, Integer ageGoe) {
        this.lastName = lastName;
        this.ageLoe = ageLoe;
        this.ageGoe = ageGoe;
    }
}

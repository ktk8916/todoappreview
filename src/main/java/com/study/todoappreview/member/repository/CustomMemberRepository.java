package com.study.todoappreview.member.repository;

import com.study.todoappreview.member.domain.dto.MemberCondition;
import com.study.todoappreview.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomMemberRepository {

    public Page<Member> findAllByCondition(MemberCondition memberCondition, Pageable pageable);
}

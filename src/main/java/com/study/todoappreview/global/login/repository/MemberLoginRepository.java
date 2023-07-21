package com.study.todoappreview.global.login.repository;

import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberLoginRepository extends JpaRepository<MemberLogin, Long> {

//    @Query("select ml.member from MemberLogin ml where ml.member.id = :memberId  order by ml.id desc limit 1")
//    Optional<Member> findMemberByMemberId(@Param("memberId") Long memberId);

    Optional<MemberLogin> findFirstByMemberIdOrderByEndedAtDesc(Long memberId);

}

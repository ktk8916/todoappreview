package com.study.todoappreview.member.repository;

import com.study.todoappreview.member.domain.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
    @Query("select m from Member m left join fetch m.todos t")
    Page<Member> findAllBy(Pageable pageable);
}

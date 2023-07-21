package com.study.todoappreview.member.domain.entity;

import com.study.todoappreview.global.login.domain.entity.MemberLogin;
import com.study.todoappreview.todo.domain.entity.Todo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private Integer age;
    @OneToMany(mappedBy = "member")
    private List<Todo> todos = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<MemberLogin> memberLogins = new ArrayList<>();
}

package com.study.todoappreview.todo.domain.entity;

import com.study.todoappreview.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Todo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private boolean isDone;
    private Integer likeCount;
    @ManyToOne
    private Member member;

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void completeTodo(){
        isDone = true;
    }

    public static Todo createTodo(Long memberId, String title, String content){
        Member member = Member.builder()
                .id(memberId)
                .build();

        return Todo.builder()
                .title(title)
                .content(content)
                .isDone(false)
                .likeCount(0)
                .member(member)
                .build();
    }
}

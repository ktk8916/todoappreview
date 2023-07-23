package com.study.todoappreview.todo.service;

import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.domain.request.TodoRequest;
import com.study.todoappreview.todo.domain.response.TodoResponse;
import com.study.todoappreview.todo.repository.TodoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final EntityManager em;

    public Todo findById(Long id){
        Optional<Todo> todo = todoRepository.findById(id);
        return todo.orElseThrow(()->new RuntimeException("낫파운드투두로바꿀거임"));
    }
    
    //본인 글이 맞는지 확인하는 메서드
    private boolean checkValidTodoMember(MemberDto memberDto, Todo todo){
        if(memberDto.id().equals(todo.getMember().getId())){
            return true;
        } else {
            throw new RuntimeException("사용자가 다름으로 바꿀거임");
        }
    }

    public void completeTodo(MemberDto memberDto, Long todoId){
        Todo todo = findById(todoId);

        if(checkValidTodoMember(memberDto, todo)){
            todo.completeTodo();
        }
    }
    public void insert(MemberDto memberDto, TodoRequest todoRequest){
        Todo todo = Todo.createTodo(
                memberDto.id(),
                todoRequest.title(),
                todoRequest.content());

        todoRepository.save(todo);
    }

    public void like(MemberDto memberDto, Long todoId) {

        Todo todo = findById(todoId);
        //like테이블 만들고 memberdto로 추가하는 과정 넣어야 함
        todo.increaseLikeCount();

    }

    public Page<TodoResponse> searchTodoByCondition(String title, String content, Boolean isDone, Integer likeLoe, Integer likeGoe, PageRequest pageRequest) {

        //컨텐츠는 항상 like 고정
        StringBuilder jpql = new StringBuilder("select t from Todo t where t.content like :content");
        StringBuilder countJpql = new StringBuilder("select count(t) from Todo t where t.content like :content");
        Map<String, Object> params = new HashMap<>();

        if (title != null) {
            jpql.append(" and t.title = :title");
            countJpql.append(" and t.title = :title");
            params.put("title", title);
        }

        if (isDone != null) {
            jpql.append(" and t.isDone = :isDone");
            countJpql.append(" and t.isDone = :isDone");
            params.put("isDone", isDone);
        }

        if(likeLoe!=null && likeGoe!=null){
            jpql.append(" and t.likeCount >= :likeLoe and t.likeCount <= :likeGoe");
            countJpql.append(" and t.likeCount >= :likeLoe and t.likeCount <= :likeGoe");
            params.put("likeGoe", likeGoe);
            params.put("likeLoe", likeLoe);
        } else if(likeGoe!=null){
            jpql.append(" and t.likeCount >= :likeGoe");
            countJpql.append(" and t.likeCount >= :likeGoe");
            params.put("likeGoe", likeGoe);
        } else if(likeLoe!=null){
            jpql.append(" and t.likeCount <= :likeLoe");
            countJpql.append(" and t.likeCount <= :likeLoe");
            params.put("likeLoe", likeLoe);
        }

        TypedQuery<Todo> query = em.createQuery(jpql.toString(), Todo.class);
        TypedQuery<Long> countQuery = em.createQuery(countJpql.toString(), Long.class);

        query.setParameter("content", "%" + content + "%");
        countQuery.setParameter("content", "%" + content + "%");

        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
            countQuery.setParameter(param, params.get(param));
        }

        long size = countQuery.getSingleResult();

        List<Todo> todos = query
                .setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        return new PageImpl<>(todos, pageRequest, size).map(TodoResponse::from);
    }
}

package com.study.todoappreview.todo.service;

import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.domain.request.TodoRequest;
import com.study.todoappreview.todo.domain.response.TodoResponse;
import com.study.todoappreview.todo.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

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

    public Page<TodoResponse> searchTodo(String title, String content, Integer likeLoe, Integer likeGoe, PageRequest pageRequest) {

        //1 컨텐츠는 항상 like 이건 고정하자..
        return todoRepository
                .findByContentLike("%"+content+"%", pageRequest)
                .map(TodoResponse::from);
    }
}

package com.study.todoappreview.todo.controller;

import com.study.todoappreview.global.aspect.TokenRequired;
import com.study.todoappreview.global.auth.AuthService;
import com.study.todoappreview.global.login.domain.dto.MemberDto;
import com.study.todoappreview.todo.domain.request.TodoRequest;
import com.study.todoappreview.todo.domain.response.TodoResponse;
import com.study.todoappreview.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;
    private final AuthService authService;

    @GetMapping
    public Page<TodoResponse> searchTodo(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "content", required = false, defaultValue = "") String content,
            @RequestParam(value = "isDone", required = false) Boolean isDone,
            @RequestParam(value = "likeLoe", required = false) Integer likeLoe,
            @RequestParam(value = "likeGoe", required = false) Integer likeGoe,
            @RequestParam(value = "page" , required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size" , required = false, defaultValue = "20") Integer size
    ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return todoService.searchTodoByCondition(
                title,
                content,
                isDone,
                likeLoe,
                likeGoe,
                pageRequest);
    }

    @PutMapping("/{todoId}/check")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @TokenRequired
    public void checkTodo(
            @PathVariable("todoId") Long todoId,
            @RequestHeader("Authorization") String token
    ){
        MemberDto memberDto = authService.tokenToMemberDto(token);
        todoService.completeTodo(memberDto, todoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @TokenRequired
    public void insert(
            @RequestHeader("Authorization") String token,
            @RequestBody TodoRequest todoRequest
    ){
        MemberDto memberDto = authService.tokenToMemberDto(token);
        todoService.insert(memberDto, todoRequest);
    }

    @PutMapping("/{todoId}/like")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @TokenRequired
    public void like(
            @RequestHeader("Authorization") String token,
            @PathVariable("todoId") Long todoId
    ){
        MemberDto memberDto = authService.tokenToMemberDto(token);
        todoService.like(memberDto, todoId);
    }

}

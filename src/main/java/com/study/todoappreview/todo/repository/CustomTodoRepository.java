package com.study.todoappreview.todo.repository;

import com.study.todoappreview.todo.domain.dto.TodoCondition;
import com.study.todoappreview.todo.domain.entity.Todo;
import com.study.todoappreview.todo.domain.response.TodoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomTodoRepository {

    List<Todo> findAllByCondition(TodoCondition todoCondition, Pageable pageable);
    Page<TodoResponse> findAllByConditionToPage(TodoCondition todoCondition, Pageable pageable);
}

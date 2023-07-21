package com.study.todoappreview.todo.repository;

import com.study.todoappreview.todo.domain.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Page<Todo> findByContentLike(String content, Pageable pageable);
}

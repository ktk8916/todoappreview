package com.study.todoappreview.todo.repository;

import com.study.todoappreview.todo.domain.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long>, CustomTodoRepository {


}

package com.lukcrow.todoapi.repositories;

import java.util.List;

import com.lukcrow.todoapi.models.Todo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findByUserID(int userID);
    Todo findByUserIDAndId(int userID, int id);
}
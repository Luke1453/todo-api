package com.lukcrow.todoapi.repositories;

import java.util.List;

import com.lukcrow.todoapi.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findByEmail(String email);

}
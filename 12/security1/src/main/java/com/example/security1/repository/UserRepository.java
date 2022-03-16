package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CURD 함수를 JpaRepository가 들고 있음
// @Repository 어노테이션 필요 X -> JpaRepository 를 통해 가능
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

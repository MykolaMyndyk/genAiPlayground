package com.example.genaiplayground.repository;

import com.example.genaiplayground.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthUser findByEmail(String email);
}

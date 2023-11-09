package com.example.genaiplayground.repository;

import com.example.genaiplayground.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    // Custom query methods if needed

    Optional<AuthUser> findByEmail(String email);
}

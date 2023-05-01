package com.example.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.security.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}

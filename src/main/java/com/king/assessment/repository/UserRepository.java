package com.king.assessment.repository;

import com.king.assessment.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findBySessionToken(String sessionToken);
}

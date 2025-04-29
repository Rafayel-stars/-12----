package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNoAndPassword(String no, String password);
}
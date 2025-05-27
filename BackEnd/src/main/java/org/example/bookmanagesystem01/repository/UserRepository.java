package org.example.bookmanagesystem01.repository;

import jakarta.persistence.OrderBy;
import org.example.bookmanagesystem01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNo(String no); // 新增方法
    @OrderBy("registerTime ASC") // 新增排序注解
    List<User> findByName(String name);
    @OrderBy("registerTime ASC") // 新增排序注解
    List<User> findByPhone(String phone);
    Optional<User> findByNoAndPassword(String no, String password);

    // 新增方法，用于检查 no 是否存在
    boolean existsByNo(String no);
}
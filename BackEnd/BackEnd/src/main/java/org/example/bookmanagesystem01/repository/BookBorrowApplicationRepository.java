package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.BookBorrowApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookBorrowApplicationRepository extends JpaRepository<BookBorrowApplication, Integer> {
    // JpaRepository已经提供了基本的CRUD方法，无需额外定义insert方法
    List<BookBorrowApplication> findByBorrowerNo(String borrowerNo);
    static void deleteAllById(List<Integer> ids){};

}
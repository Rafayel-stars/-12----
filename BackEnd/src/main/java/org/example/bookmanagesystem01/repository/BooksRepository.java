package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BooksRepository extends JpaRepository<Books, Integer> {
    List<Books> findByTitleContaining(String title);
}
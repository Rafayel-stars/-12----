package org.example.bookmanagesystem01.repository;

import jakarta.persistence.OrderBy;
import org.example.bookmanagesystem01.entity.Books;
import org.example.bookmanagesystem01.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findByLibraryCode(String librarycode);
    @OrderBy("entryTime ASC") // 新增排序注解
    List<Books> findByIsbn(String isbn);
    @OrderBy("entryTime ASC") // 新增排序注解
    List<Books> findByTitle(String title);
    @OrderBy("entryTime ASC") // 新增排序注解
    List<Books> findByAuthor(String author);
    @OrderBy("entryTime ASC") // 新增排序注解
    List<Books> findByCategoryContaining(String category);
}
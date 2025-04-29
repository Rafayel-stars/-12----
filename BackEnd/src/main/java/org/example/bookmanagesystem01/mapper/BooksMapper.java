package org.example.bookmanagesystem01.mapper;

import org.example.bookmanagesystem01.entity.Books;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BooksMapper {
    Books getBookById(int id);
    void addBook(Books book);
    void updateBook(Books book);
    void deleteBook(int id);
    List<Books> searchBooks(String title);
    void changeBookStatus(Map<String, Object> params);
}
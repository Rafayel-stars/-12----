package org.example.bookmanagesystem01.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String isbn;
    @Column(name = "library_code")
    private String libraryCode; // 图书馆自编编号，对应数据库的 library_code 字段
    private String title;
    private String author;
    private String category;
    private int status;
    private Date entryTime;

    public Books() {
    }

    public Books(String librarycode,String isbn, String title, String author, String category, int status,Date entrytime) {
        this.libraryCode=librarycode;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.status = status;
        this.entryTime=entrytime;
    }

    public String getLibraryCode() {
        return libraryCode;
    }

    public void setLibraryCode(String libraryCode) {
        this.libraryCode = libraryCode;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", libraryCode='" + libraryCode + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                ", entryTime=" + entryTime +
                '}';
    }
}
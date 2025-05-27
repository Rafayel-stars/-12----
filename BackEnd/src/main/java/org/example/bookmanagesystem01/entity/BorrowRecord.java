package org.example.bookmanagesystem01.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bookId;
    private String borrowerName;
    private Date borrowTime;
    private Date returnTime;
    private int status;
    private String adminId;

    public BorrowRecord() {
    }

    public BorrowRecord(String bookId, String borrowerName, String adminId) {
        this.bookId = bookId;
        this.borrowerName = borrowerName;
        this.adminId = adminId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", borrowerName='" + borrowerName + '\'' +
                ", borrowTime=" + borrowTime +
                ", returnTime=" + returnTime +
                ", status=" + status +
                ", adminId=" + adminId +
                '}';
    }
}
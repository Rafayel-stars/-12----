package org.example.bookmanagesystem01.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class BorrowRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bookId;
    private String borrowerName;
    private Date borrowTime;
    private Date returnTime;
    private int status;
    private int adminId;

    public BorrowRecord() {
    }

    public BorrowRecord(int bookId, String borrowerName, int adminId) {
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

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
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

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
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
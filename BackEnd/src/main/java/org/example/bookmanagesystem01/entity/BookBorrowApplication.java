package org.example.bookmanagesystem01.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class BookBorrowApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String libraryCode;       // 图书馆编号
    private String borrowerNo;        // 借书人账号
    private Date borrowDate;          // 借书日期（原申请日期，关键修改）
    private Date expectedReturnDate;  // 预计归还时间
    private Integer status;            // 申请状态
    @Column(name = "reviewer_no", nullable = false)
    private String reviewerNo;        // 审核人账号

    public BookBorrowApplication() {
    }

    public BookBorrowApplication(Integer id, String libraryCode, String borrowerNo, Date borrowDate, Date expectedReturnDate, Integer status, String reviewerNo) {
        this.id = id;
        this.libraryCode = libraryCode;
        this.borrowerNo = borrowerNo;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.status = status;
        this.reviewerNo = reviewerNo;
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLibraryCode() { return libraryCode; }
    public void setLibraryCode(String libraryCode) { this.libraryCode = libraryCode; }

    public String getBorrowerNo() { return borrowerNo; }
    public void setBorrowerNo(String borrowerNo) { this.borrowerNo = borrowerNo; }

    public Date getBorrowDate() { return borrowDate; }  // 关键修改：applyDate → borrowDate
    public void setBorrowDate(Date borrowDate) { this.borrowDate = borrowDate; }

    public Date getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(Date expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getReviewerNo() { return reviewerNo; }
    public void setReviewerNo(String reviewerNo) { this.reviewerNo = reviewerNo; }

    @Override
    public String toString() {
        return "BookBorrowApplication{" +
                "id=" + id +
                ", libraryCode='" + libraryCode + '\'' +
                ", borrowerNo='" + borrowerNo + '\'' +
                ", borrowDate=" + borrowDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", status=" + status +
                ", reviewerNo='" + reviewerNo + '\'' +
                '}';
    }
}
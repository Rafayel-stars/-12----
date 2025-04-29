package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    List<BorrowRecord> findByBorrowerName(String borrower_name);
}
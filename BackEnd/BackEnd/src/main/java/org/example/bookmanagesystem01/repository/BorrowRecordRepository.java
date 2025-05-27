package org.example.bookmanagesystem01.repository;

import jakarta.persistence.OrderBy;
import org.example.bookmanagesystem01.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    @OrderBy("entryTime DESC") // 新增排序注解
    List<BorrowRecord> findByBorrowerName(String borrower_name);
}
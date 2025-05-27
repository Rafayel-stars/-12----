package org.example.bookmanagesystem01.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.bookmanagesystem01.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowRecordMapper {
    BorrowRecord getBorrowRecordById(int id);
    void addBorrowRecord(BorrowRecord borrowRecord);
    void updateBorrowRecord(BorrowRecord borrowRecord);
    void deleteBorrowRecord(int id);
    List<BorrowRecord> findByBorrowerName(String borrowerName);

    // 在接口中添加批量删除方法
    void deleteBatch(@Param("ids") List<Integer> ids);
}
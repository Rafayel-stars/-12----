package org.example.bookmanagesystem01.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.bookmanagesystem01.entity.BookBorrowApplication;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookBorrowApplicationMapper {
    void insert(BookBorrowApplication application);          // 插入申请
    void update(BookBorrowApplication application);          // 更新申请
    void deleteById(Integer id);                            // 删除申请
    BookBorrowApplication selectById(Integer id);            // 按ID查询
    List<BookBorrowApplication> selectByBorrowerNo(String borrowerNo); // 按借书人账号查询
    List<BookBorrowApplication> selectAll();                 // 查询所有（管理员权限）

    // 批量删除（在接口中添加）
    void deleteBatch(@Param("ids") List<Integer> ids);
}
package org.example.bookmanagesystem01.controller;

import org.example.bookmanagesystem01.entity.Books;
import org.example.bookmanagesystem01.entity.BorrowRecord;
import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.example.bookmanagesystem01.repository.BooksRepository;
import org.example.bookmanagesystem01.repository.BorrowRecordRepository;
import org.example.bookmanagesystem01.repository.SpaceApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private SpaceApplicationRepository spaceApplicationRepository;

    // 搜索图书查看图书信息
    @GetMapping("/book/search")
    public List<Books> searchBooks(@RequestParam String title) {
        return booksRepository.findByTitleContaining(title);
    }

    // 查看自己的借阅记录
    @GetMapping("/borrow/records")
    public List<BorrowRecord> getMyBorrowRecords(@RequestParam String borrower_name) {
        return borrowRecordRepository.findByBorrowerName(borrower_name);
    }

    // 填写提交空间申请表
    @PostMapping("/space/application/add")
    public SpaceApplication addSpaceApplication(@RequestBody SpaceApplication application) {
        return spaceApplicationRepository.save(application);
    }

    // 查看空间申请表的状态
    @GetMapping("/space/application/status")
    public List<SpaceApplication> getSpaceApplicationStatus(@RequestParam String applicant_name) {
        return spaceApplicationRepository.findByApplicantName(applicant_name);
    }
}
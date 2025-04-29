package org.example.bookmanagesystem01.controller;

import org.example.bookmanagesystem01.entity.Books;
import org.example.bookmanagesystem01.entity.BorrowRecord;
import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.example.bookmanagesystem01.entity.User;
import org.example.bookmanagesystem01.repository.BooksRepository;
import org.example.bookmanagesystem01.repository.BorrowRecordRepository;
import org.example.bookmanagesystem01.repository.SpaceApplicationRepository;
import org.example.bookmanagesystem01.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private SpaceApplicationRepository spaceApplicationRepository;

    // 新增用户
    @PostMapping("/user/add")
    public User addUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 删除用户
    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    // 修改用户
    @PutMapping("/user/update")
    public User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 查询用户
    @GetMapping("/user/get/{id}")
    public User getUser(@PathVariable Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // 新增图书
    @PostMapping("/book/add")
    public Books addBook(@RequestBody Books book) {
        return booksRepository.save(book);
    }

    // 删除图书
    @DeleteMapping("/book/delete/{id}")
    public void deleteBook(@PathVariable Integer id) {
        booksRepository.deleteById(id);
    }

    // 修改图书
    @PutMapping("/book/update")
    public Books updateBook(@RequestBody Books book) {
        return booksRepository.save(book);
    }

    // 查询图书
    @GetMapping("/book/get/{id}")
    public Books getBook(@PathVariable Integer id) {
        return booksRepository.findById(id).orElse(null);
    }

    // 搜索图书
    @GetMapping("/book/search")
    public List<Books> searchBooks(@RequestParam String title) {
        return booksRepository.findByTitleContaining(title);
    }

    // 更改图书借阅状态
    @PutMapping("/book/status/{id}/{status}")
    public Books changeBookStatus(@PathVariable Integer id, @PathVariable Integer status) {
        Books book = booksRepository.findById(id).orElse(null);
        if (book != null) {
            book.setStatus(status);
            return booksRepository.save(book);
        }
        return null;
    }

    // 填写借阅记录表
    @PostMapping("/borrow/add")
    public BorrowRecord addBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        return borrowRecordRepository.save(borrowRecord);
    }

    // 查看搜索所有读者的借阅记录
    @GetMapping("/borrow/search")
    public List<BorrowRecord> searchBorrowRecords(@RequestParam(required = false) String borrower_name) {
        if (borrower_name != null) {
            return borrowRecordRepository.findByBorrowerName(borrower_name);
        }
        return borrowRecordRepository.findAll();
    }

    // 修改空间申请表审核状态
    @PutMapping("/space/application/status/{id}/{status}")
    public SpaceApplication changeSpaceApplicationStatus(@PathVariable Integer id, @PathVariable Integer status) {
        SpaceApplication application = spaceApplicationRepository.findById(id).orElse(null);
        if (application != null) {
            application.setStatus(status);
            return spaceApplicationRepository.save(application);
        }
        return null;
    }
}

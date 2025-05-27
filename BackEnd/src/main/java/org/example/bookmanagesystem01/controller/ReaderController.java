package org.example.bookmanagesystem01.controller;

import org.example.bookmanagesystem01.entity.*;
import org.example.bookmanagesystem01.mapper.AnnouncementMapper;
import org.example.bookmanagesystem01.mapper.BookBorrowApplicationMapper;
import org.example.bookmanagesystem01.mapper.LibrarySpaceMapper;
import org.example.bookmanagesystem01.repository.*;
import org.example.bookmanagesystem01.utils.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private SpaceApplicationRepository spaceApplicationRepository;
    @Autowired
    private AuthenticationFacade authenticationFacade;  // 安全框架获取当前用户

    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private LibrarySpaceRepository librarySpaceRepository;
    @Autowired
    private BookBorrowApplicationRepository bookBorrowApplicationRepository;


    // 查询图书
    @GetMapping({"/book/get/{librarycode}","/book/get"})
    public Object getBook(@PathVariable(required = false) String librarycode){
        if(librarycode != null){
            return booksRepository.findByLibraryCode(librarycode).orElse(null);
        }else{
            return booksRepository.findAll();
        }
    }

    @GetMapping("/book/searchbytitle/{title}")
    public List<Books> searchByTitle(@PathVariable String title) {
        return booksRepository.findByTitle(title);
    }

    @GetMapping("/book/searchbyisbn/{isbn}")
    public List<Books> searchByIsbn(@PathVariable String isbn) {
        return booksRepository.findByIsbn(isbn);
    }

    @GetMapping("/book/searchbyauthor/{author}")
    public List<Books> searchByAuthor(@PathVariable String author) {
        return booksRepository.findByAuthor(author);
    }

    @GetMapping("/book/searchbycategory/{category}")
    public List<Books> searchByCategory(@PathVariable String category) {
        return booksRepository.findByCategoryContaining(category);
    }

    // 查看自己的借阅记录
    @GetMapping("/borrow/records")
    public List<BorrowRecord> getMyBorrowRecords() {
        // 从认证信息中获取当前用户账号（需先确保AuthenticationFacade已正确注入）
        String currentUserNo = authenticationFacade.getAuthentication().getName();
        return borrowRecordRepository.findByBorrowerName(currentUserNo);
    }

    // 填写提交空间申请表（强制设置status为0）
    @PostMapping("/space/application/add")
    public SpaceApplication addSpaceApplication(@RequestBody SpaceApplication application) {
        // 强制覆盖status为0，忽略前端传入的值
        application.setStatus(0);
        return spaceApplicationRepository.save(application);
    }

    // 查看空间申请表的状态
    @GetMapping("/space/application/status")
    public List<SpaceApplication> getMySpaceApplicationStatus() {
        String currentUserNo = authenticationFacade.getAuthentication().getName();
        return spaceApplicationRepository.findByApplicantName(currentUserNo);
    }

    @PostMapping("/borrowapplication/submit")
    public ResponseEntity<BookBorrowApplication> submitApplication(
            @RequestBody BookBorrowApplication application
    ) {
        // 获取当前读者账号（从安全上下文中获取，如Spring Security的Username）
        application.setStatus(0);
        String currentUserNo = authenticationFacade.getAuthentication().getName();
        application.setBorrowerNo(currentUserNo);  // 强制覆盖，防止前端篡改
        application.setReviewerNo("moren");            // 读者不可设置审核人

        // 使用JPA的save方法替代MyBatis的insert
        BookBorrowApplication savedApplication = bookBorrowApplicationRepository.save(application);

        return ResponseEntity.ok(savedApplication);
    }

    // 查看：读者查看自己的申请状态
    @GetMapping("/borrowapplication/my/applications")
    public ResponseEntity<List<BookBorrowApplication>> getMyApplications() {
        String currentUserNo = authenticationFacade.getAuthentication().getName();
        // 使用 JPA Repository 方法替代 MyBatis Mapper
        List<BookBorrowApplication> applications = bookBorrowApplicationRepository.findByBorrowerNo(currentUserNo);
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/announcement/detail/{id}")
    public ResponseEntity<Announcement> getAnnouncementDetail(@PathVariable Integer id) {
        // 使用 JPA Repository 的 findById 方法（返回 Optional）
        Optional<Announcement> announcementOptional = announcementRepository.findById(id);

        if (announcementOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(announcementOptional.get());
    }
    @GetMapping("/announcement/list/status/{status}")
    public ResponseEntity<List<Announcement>> getAnnouncementsByStatus(@PathVariable Integer status) {
        // 调用 JPA Repository 方法查询指定状态的公告列表
        List<Announcement> announcements = announcementRepository.findByStatus(status);

        // 检查结果是否为空
        if (announcements.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 返回成功响应和公告列表
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/space/list")
    public List<LibrarySpace> getAllSpaces() {
        return librarySpaceRepository.findAll(); // 使用 JPA 的 findAll() 方法
    }
}
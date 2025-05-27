package org.example.bookmanagesystem01.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Param;
import org.example.bookmanagesystem01.entity.*;
import org.example.bookmanagesystem01.mapper.*;
import org.example.bookmanagesystem01.repository.*;
import org.example.bookmanagesystem01.utils.UpdateStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

import org.example.bookmanagesystem01.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BooksRepository booksRepository;
    @Autowired
    private BorrowRecordRepository borrowRecordRepository;
    @Autowired
    private SpaceApplicationRepository spaceApplicationRepository;
    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private BookBorrowApplicationMapper bookBorrowApplicationMapper; // 添加此注入
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;
    @Autowired
    private SpaceApplicationMapper spaceApplicationMapper;
    @Autowired
    private LibrarySpaceMapper librarySpaceMapper;
    @Autowired
    private AnnouncementRepository announcementRepository;
    @Autowired
    private BookBorrowApplicationRepository bookBorrowApplicationRepository;

    // 新增代码：注入 PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 新增用户
    @PostMapping("/user/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 加密密码

        if (userService.saveUser(user)) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("用户账号 no 已存在，添加失败");
        }
    }

    // 删除用户
    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    // 批量删除用户
    @DeleteMapping("/user/delete/batch")
    public ResponseEntity<?> deleteUsers(@RequestBody List<Integer> ids) {  // 直接接收数组
        try{
            userRepository.deleteAllById(ids);
            return ResponseEntity.ok("删除成功");
    } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("批量删除用户失败: " + e.getMessage());
        }
    }

    // 修改用户
    @PutMapping("/user/update")
    public User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // 查询用户
    @GetMapping({"/user/get/{no}", "/user/get"})
    public Object getUser(@PathVariable(required = false) String no) {
        if (no != null) {
            return userRepository.findByNo(no).orElse(null);
        } else {
            return userRepository.findAll();
        }
    }

    @GetMapping("/user/getByName/{name}")
    public List<User> getUserByName(@PathVariable String name) {
        return userRepository.findByName(name);
    }

    @GetMapping("/user/getByPhone/{phone}")
    public List<User> getUserByPhone(@PathVariable String phone) {
        return userRepository.findByPhone(phone);
    }

    // 新增图书
    @PostMapping("/book/add")
    public Books addBook(@RequestBody Books book) {
        return booksRepository.save(book);
    }

    // 删除图书
    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable int id) {
        booksRepository.deleteById(id);
    }

    // 批量删除图书
    @DeleteMapping("/book/delete/batch")
    public ResponseEntity<?> deleteBooks(@RequestBody List<Integer> ids) {
        try {
            booksRepository.deleteAllById(ids);
            return ResponseEntity.ok("批量删除图书成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量删除图书失败: " + e.getMessage());
        }
    }

    // 修改图书
    @PutMapping("/book/update")
    public Books updateBook(@RequestBody Books book) {
        return booksRepository.save(book);
    }


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
    @GetMapping({"/borrow/search/{borrower_name}","/borrow/search"})
    public List<BorrowRecord> searchBorrowRecords(@PathVariable(required = false) String borrower_name) {
        if (borrower_name != null) {
            return borrowRecordRepository.findByBorrowerName(borrower_name);
        }
        return borrowRecordRepository.findAll();
    }

    // 单个删除借阅记录
    @DeleteMapping("/borrow/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBorrowRecord(@PathVariable int id) {
        borrowRecordRepository.deleteById(id); // 直接使用JPA的deleteById（若使用MyBatis需调用Mapper）
    }

    // 批量删除借阅记录（MyBatis版本）
    @DeleteMapping("/borrow/delete/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBorrowRecords(@RequestBody List<Integer> ids) {
        try {

            borrowRecordRepository.deleteAllById(ids);
            return ResponseEntity.ok("批量删除借阅记录成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量删除借阅记录失败: " + e.getMessage());
        }
    }

    @GetMapping({"/space/application/status/{applicant_name}", "/space/application/status"})
    public List<SpaceApplication> getSpaceApplicationStatus(
            @PathVariable(required = false) String applicant_name) { // 设置参数为可选
        if (applicant_name != null) {
            return spaceApplicationRepository.findByApplicantName(applicant_name);
        }
        return spaceApplicationRepository.findAll();
    }

    @GetMapping("/space/application/detail/{id}")
    public ResponseEntity<SpaceApplication> getSpaceApplicationById(@PathVariable Integer id) {
        return spaceApplicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

    // 单个删除空间申请记录
    @DeleteMapping("/space/application/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSpaceApplication(@PathVariable int id) {
        spaceApplicationRepository.deleteById(id); // 直接使用JPA的deleteById
    }

    // 批量删除空间申请记录
    @DeleteMapping("/space/application/delete/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSpaceApplications(@RequestBody List<Integer> ids) {
        try {

            spaceApplicationRepository.deleteAllById(ids);
            return ResponseEntity.ok("批量删除空间申请记录成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量删除空间申请记录失败: " + e.getMessage());
        }
    }

    // 新增通知（修改调用对象）
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/announcement/add")
    public Announcement addAnnouncement(@RequestBody Announcement announcement) {
        return announcementRepository.save(announcement); // 使用 JPA 的 save 方法
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/announcement/delete/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Integer id) {
        try {
            // 检查公告是否存在
            if (!announcementRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // 使用 JPA 的 deleteById 方法删除
            announcementRepository.deleteById(id);
            return ResponseEntity.ok("公告删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("公告删除失败: " + e.getMessage());
        }
    }

    @PutMapping("/announcement/{id}")
    public Announcement updateAnnouncement(
            @PathVariable Integer id,
            @RequestBody Announcement updatedAnnouncement
    ) {
        Announcement announcement = announcementRepository.findById(id).orElse(null);
        if (announcement != null) {
            // 更新可修改的属性
            if (updatedAnnouncement.getTitle() != null) {
                announcement.setTitle(updatedAnnouncement.getTitle());
            }
            if (updatedAnnouncement.getContent() != null) {
                announcement.setContent(updatedAnnouncement.getContent());
            }
            if (updatedAnnouncement.getPicture() != null) {
                announcement.setPicture(updatedAnnouncement.getPicture());
            }
            if (updatedAnnouncement.getAuthor() != null) {
                announcement.setAuthor(updatedAnnouncement.getAuthor());
            }
            if (updatedAnnouncement.getStatus() != 0) { // 假设状态默认为0或无效值
                announcement.setStatus(updatedAnnouncement.getStatus());
            }

            return announcementRepository.save(announcement);
        }
        return null;
    }

    // 按状态查询通知
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping({"/announcement/status/{status}","/announcement/status"})
    public List<Announcement> getAnnouncementsByStatus(@PathVariable(required = false) Integer status) {
        if(status != null){
            return announcementRepository.findByStatus(status);
        }else{
            return announcementRepository.findAllByOrderByPublishtimeDesc();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/borrowapplication/create")
    public ResponseEntity<BookBorrowApplication> createApplication(
            @RequestBody @Valid BookBorrowApplication application
    ) {
        // 设置默认状态（使用整数，如 0 代表待审核）
        application.setStatus(0); // 0: 待审核
        application.setReviewerNo("moren");
        BookBorrowApplication savedApplication = bookBorrowApplicationRepository.save(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }

    @PutMapping("/borrowapplication/update/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookBorrowApplication> updateStatus(@RequestBody UpdateStatusRequest request) {
        // 获取当前管理员账号（从安全上下文）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentAdminNo = authentication.getName();

        // 查询借阅申请
        BookBorrowApplication application = bookBorrowApplicationRepository.findById(request.getId())
                .orElse(null);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        // 设置状态和审核员
        application.setStatus(request.getStatus());
        application.setReviewerNo(currentAdminNo); // 确保此处正确设置

        // 保存更新
        BookBorrowApplication updatedApplication = bookBorrowApplicationRepository.saveAndFlush(application);

        return ResponseEntity.ok(updatedApplication);
    }

    @GetMapping("/borrowapplication/search/borrower")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookBorrowApplication>> searchByBorrowerNo(
            @RequestParam(required = false) String borrowerNo
    ) {
        if (borrowerNo != null) {
            return ResponseEntity.ok(bookBorrowApplicationRepository.findByBorrowerNo(borrowerNo));
        }
        return ResponseEntity.ok(bookBorrowApplicationRepository.findAll());
    }

    // 删除：管理员删除申请
    @DeleteMapping("/borrowapplication/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        // 使用JPA的existsById检查记录是否存在
        if (!bookBorrowApplicationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // 使用JPA的deleteById方法删除记录
        bookBorrowApplicationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 批量删除借阅申请表
    @DeleteMapping("/borrowapplication/delete/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteBorrowApplications(@RequestBody List<Integer> ids) {
        try {
            // 调用批量删除方法（需在Mapper中新增批量删除逻辑）
//            bookBorrowApplicationMapper.deleteBatch(ids);
            BookBorrowApplicationRepository.deleteAllById(ids);
            return ResponseEntity.ok("批量删除借阅申请表成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("批量删除借阅申请表失败: " + e.getMessage());
        }
    }

    // 新增：管理员查看所有空间
    @GetMapping("/space/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<LibrarySpace> getAllSpaces() {
        return librarySpaceMapper.selectAll(); // 需在Mapper中新增selectAll方法
    }
}
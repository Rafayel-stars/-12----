package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

    // 按状态查询公告
    List<Announcement> findByStatus(Integer status);

    // 查询所有公告（按发布时间降序）
    List<Announcement> findAllByOrderByPublishtimeDesc();

    // 保留 JPA 的 update 方法（使用 JPQL）
    @Transactional
    @Modifying
    @Query("UPDATE Announcement a SET a.title = :#{#announcement.title}, a.content = :#{#announcement.content} WHERE a.id = :#{#announcement.id}")
    int updateAnnouncement(Announcement announcement);
}
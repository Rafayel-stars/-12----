package org.example.bookmanagesystem01.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.bookmanagesystem01.entity.Announcement;
import java.util.List;

@Mapper
public interface AnnouncementMapper {
    int addAnnouncement(Announcement announcement);
    int deleteAnnouncement(int id);
    int updateAnnouncement(Announcement announcement);
    List<Announcement> getAnnouncementsByStatus(int status);
    // 根据ID查询通知详情
    Announcement selectById(int id);
    // 查询所有公告
    List<Announcement> findAll();
}

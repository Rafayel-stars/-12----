package org.example.bookmanagesystem01.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Mapper
public interface SpaceApplicationMapper {
    SpaceApplication getSpaceApplicationById(int applicationId);
    void addSpaceApplication(SpaceApplication spaceApplication);
    void updateSpaceApplication(SpaceApplication spaceApplication);
    void deleteSpaceApplication(int applicationId);
    List<SpaceApplication> findByApplicantName(String applicantName);
    void changeSpaceApplicationStatus(Map<String, Object> params);

    // 在接口中添加批量删除方法
    void deleteBatch(@Param("ids") List<Integer> ids);
}
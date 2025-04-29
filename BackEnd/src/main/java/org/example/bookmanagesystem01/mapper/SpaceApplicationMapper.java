package org.example.bookmanagesystem01.mapper;

import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.apache.ibatis.annotations.Mapper;

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
}
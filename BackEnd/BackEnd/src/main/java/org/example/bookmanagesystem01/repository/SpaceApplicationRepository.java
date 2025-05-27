package org.example.bookmanagesystem01.repository;

import jakarta.persistence.OrderBy;
import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceApplicationRepository extends JpaRepository<SpaceApplication, Integer> {
    @OrderBy("entryTime DESC") // 新增排序注解
    List<SpaceApplication> findByApplicantName(String applicant_name);
}

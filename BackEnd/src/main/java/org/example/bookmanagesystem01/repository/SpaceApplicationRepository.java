package org.example.bookmanagesystem01.repository;

import org.example.bookmanagesystem01.entity.SpaceApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceApplicationRepository extends JpaRepository<SpaceApplication, Integer> {
    List<SpaceApplication> findByApplicantName(String applicant_name);
}

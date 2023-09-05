package com.semestrial_project.logistic_company.domain.repository;

import com.semestrial_project.logistic_company.domain.entity.DrivingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface DrivingCategoryRepository extends JpaRepository<DrivingCategory, Long> {
    Set<DrivingCategory> findDrivingCategoryBySuppliersId(Long supplierId);
}

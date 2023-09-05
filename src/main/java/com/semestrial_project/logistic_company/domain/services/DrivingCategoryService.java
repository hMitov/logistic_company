package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.entity.DrivingCategory;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;
import java.util.Set;

public interface DrivingCategoryService {

    Set<DrivingCategoryResponse> getAllDrivingCategories();
    Set<DrivingCategoryResponse> extractDrivingCategories(Set<String> drivingCategoriesNames) throws DomainException;
    Set<DrivingCategoryResponse> getDrivingCategoryBySupplierId(Long id);
}

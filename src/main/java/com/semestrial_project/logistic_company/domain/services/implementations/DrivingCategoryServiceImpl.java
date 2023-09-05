package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.entity.DrivingCategory;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.DrivingCategoryRepository;
import com.semestrial_project.logistic_company.domain.services.DrivingCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.DRIVING_CATEGORY_NOT_FOUND;

@Service
@AllArgsConstructor
public class DrivingCategoryServiceImpl implements DrivingCategoryService {

    private final DrivingCategoryRepository drivingCategoryRepository;

    private final DomainAdapter domainAdapter;

    public Set<DrivingCategoryResponse> getAllDrivingCategories() {
        return drivingCategoryRepository.findAll().stream()
                .map(domainAdapter::convertToDrivingCategoryResponse).collect(Collectors.toSet());
    }

    public Set<DrivingCategoryResponse> extractDrivingCategories(Set<String> drivingCategoriesNames) throws DomainException {
        Set<DrivingCategoryResponse> drivingCategories = new HashSet<>();
        for (String drivingCategoryName : drivingCategoriesNames) {
            DrivingCategory drivingCategory = drivingCategoryRepository.findById(DrivingCategory.getCategory(drivingCategoryName))
                    .orElseThrow(() -> new DomainException(DRIVING_CATEGORY_NOT_FOUND));
            drivingCategories.add(domainAdapter.convertToDrivingCategoryResponse(drivingCategory));
        }
        return drivingCategories;
    }

    public Set<DrivingCategoryResponse> getDrivingCategoryBySupplierId(Long id) {
        return domainAdapter.convertToDrivingCategoryResponse(
                drivingCategoryRepository.findDrivingCategoryBySuppliersId(id)
        );
    }
}

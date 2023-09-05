package com.semestrial_project.logistic_company.web.view.models.supplier;

import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SupplierViewResponse {

    private Long id;

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Long employeeId;

    private Double salary;

    private LocalDate dateOfEmploy;

    private Boolean currentEmployee;

    private Set<String> drivingLicenseCategories;

    private VehicleResponse vehicle;

}

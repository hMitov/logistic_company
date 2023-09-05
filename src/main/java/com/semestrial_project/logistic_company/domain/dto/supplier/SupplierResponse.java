package com.semestrial_project.logistic_company.domain.dto.supplier;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class SupplierResponse {

    private Long id;

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Long employeeId;

    private Boolean currentEmployee;

    private Double salary;

    private LocalDate dateOfEmploy;

    @JsonProperty("drivingLicenseCategories")
    private Set<DrivingCategoryResponse> drivingLicenseCategories;

    private VehicleResponse vehicle;

}

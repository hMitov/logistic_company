package com.semestrial_project.logistic_company.web.view.models.supplier;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UpdateSupplierView {

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Double salary;

    private LocalDate dateOfEmploy;

    private Set<String> drivingLicenseCategories;

    private Long vehicleId;

}

package com.semestrial_project.logistic_company.domain.dto.supplier;

import com.semestrial_project.logistic_company.domain.dto.office_employee.BaseEmployeeData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SupplierData extends BaseEmployeeData {

    private Set<String> drivingLicenseCategories;

    private Long vehicleId;
}

package com.semestrial_project.logistic_company.domain.dto.office_employee;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OfficeEmployeeData extends BaseEmployeeData {
    private String officeAddress;
}

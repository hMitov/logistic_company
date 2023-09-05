package com.semestrial_project.logistic_company.domain.dto.office_employee;

import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.entity.Office;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OfficeEmployeeResponse {

    private Long id;

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Boolean currentEmployee;

    private Long employeeId;

    private Double salary;

    private LocalDate dateOfEmploy;

    private OfficeResponse office;

}

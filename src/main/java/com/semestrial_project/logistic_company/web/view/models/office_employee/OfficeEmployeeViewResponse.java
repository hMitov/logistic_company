package com.semestrial_project.logistic_company.web.view.models.office_employee;

import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.web.view.models.office.OfficeViewResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OfficeEmployeeViewResponse {

    private Long id;

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Long employeeId;

    private double salary;

    private LocalDate dateOfEmploy;

    private Boolean currentEmployee;

    private OfficeViewResponse office;

}

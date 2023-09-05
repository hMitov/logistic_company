package com.semestrial_project.logistic_company.domain.dto.office_employee;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BaseEmployeeData {

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Double salary;

    private LocalDate dateOfEmploy;
}

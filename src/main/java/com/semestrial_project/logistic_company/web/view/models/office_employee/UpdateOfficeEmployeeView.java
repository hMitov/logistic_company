package com.semestrial_project.logistic_company.web.view.models.office_employee;

import lombok.Data;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class UpdateOfficeEmployeeView {

    private String egn;

    private String firstName;

    private String middleName;

    private String lastName;

    private Double salary;

    private LocalDate dateOfEmploy;

    private String officeAddress;

}

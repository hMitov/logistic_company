package com.semestrial_project.logistic_company.web.view.models.office;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OfficeViewResponse {

    private Long id;

    private AddressViewResponse officeAddress;

    private String telephone;

    private String dateOfEstablishment;

}

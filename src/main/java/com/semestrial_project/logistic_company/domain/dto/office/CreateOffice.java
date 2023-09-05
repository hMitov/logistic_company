package com.semestrial_project.logistic_company.domain.dto.office;

import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateOffice {

    private AddressRequest officeAddress;

    private String telephone;

    private LocalDate dateOfEstablishment;
}

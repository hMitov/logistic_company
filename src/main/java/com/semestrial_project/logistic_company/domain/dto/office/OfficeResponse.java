package com.semestrial_project.logistic_company.domain.dto.office;

import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OfficeResponse {

    private Long id;

    private AddressResponse officeAddress;

    private String telephone;

    private LocalDate dateOfEstablishment;
}

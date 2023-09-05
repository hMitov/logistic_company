package com.semestrial_project.logistic_company.domain.dto.office;

import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class UpdateOffice {

    private AddressRequest officeAddress;

    private String telephone;

    private String dateOfEstablishment;
}

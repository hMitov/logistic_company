package com.semestrial_project.logistic_company.domain.dto.recipient;

import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import lombok.Data;

@Data
public class RecipientRs {

    private Long id;

    private String firstName;

    private String lastName;

    private String organizationName;

    private AddressResponse address;

    private String telephone;

    private String email;

    private String specialInstructions;

}

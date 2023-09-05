package com.semestrial_project.logistic_company.web.view.models.sender;

import com.semestrial_project.logistic_company.web.view.models.address.AddressViewResponse;
import lombok.Data;

@Data
public class SenderViewResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String organizationName;

    private AddressViewResponse address;

    private String telephone;

    private String email;
}

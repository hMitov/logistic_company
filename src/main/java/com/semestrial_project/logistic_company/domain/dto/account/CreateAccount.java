package com.semestrial_project.logistic_company.domain.dto.account;

import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.user.CreateUser;
import lombok.Data;

@Data
public class CreateAccount {

    private String firstName;

    private String lastName;

    private String telephone;

    private AddressRequest address;

    private CreateUser user;
}

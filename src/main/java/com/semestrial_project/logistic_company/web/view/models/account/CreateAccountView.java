package com.semestrial_project.logistic_company.web.view.models.account;

import com.semestrial_project.logistic_company.web.view.models.user.CreateUserView;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class CreateAccountView {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String telephone;

    @NotBlank
    private String city;

    @NotBlank
    private String postCode;

    @NotBlank
    private String street;

    @NotBlank
    private String streetNum;

    @Valid
    private CreateUserView user;
}

package com.semestrial_project.logistic_company.web.view.models.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateUserView {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

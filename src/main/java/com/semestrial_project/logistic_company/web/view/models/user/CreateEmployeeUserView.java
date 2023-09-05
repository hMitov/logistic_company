package com.semestrial_project.logistic_company.web.view.models.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
public class CreateEmployeeUserView {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotEmpty
    private Set<String> roles;
}

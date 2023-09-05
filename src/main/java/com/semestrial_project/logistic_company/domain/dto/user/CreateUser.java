package com.semestrial_project.logistic_company.domain.dto.user;

import lombok.Data;

import java.util.Set;

@Data
public class CreateUser {

    private String username;

    private String password;

    private Set<String> roles;
}

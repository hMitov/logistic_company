package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.account.CreateAccount;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

public interface AccountService {

    void createAccount(CreateAccount createAccount) throws DomainException;
}

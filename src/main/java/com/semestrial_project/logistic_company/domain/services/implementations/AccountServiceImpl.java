package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.dto.account.CreateAccount;
import com.semestrial_project.logistic_company.domain.entity.Account;
import com.semestrial_project.logistic_company.domain.entity.User;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.AccountRepository;
import com.semestrial_project.logistic_company.domain.services.AccountService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final DomainValidator validator;

    private final UserDetailsServiceImpl userDetailsService;


    @Transactional(rollbackFor = DomainException.class)
    public void createAccount(CreateAccount createAccount) throws DomainException {
        validator.validateAccountOnCreate(createAccount);

        userDetailsService.createUser(createAccount.getUser());

        Account account = Account.builder()
                .firstName(createAccount.getFirstName())
                .lastName(createAccount.getLastName())
                .telephone(createAccount.getTelephone())
                .email(createAccount.getUser().getUsername())
                .city(createAccount.getAddress().getCity())
                .postCode(createAccount.getAddress().getPostCode())
                .street(createAccount.getAddress().getStreet())
                .streetNum(createAccount.getAddress().getStreetNum())
                .build();
        accountRepository.save(account);
    }
}

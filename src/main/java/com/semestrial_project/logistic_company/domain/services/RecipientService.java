package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientData;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface RecipientService {

    RecipientRs processRecipientData(RecipientData recipientData, String arrivalPoint) throws DomainException;
    RecipientRs updateRecipient(Long id, RecipientData recipientData, String arrivalPoint) throws DomainException;
    RecipientResponse getRecipientById(Long id) throws DomainException;
    List<RecipientResponse> getAllRecipients();
}


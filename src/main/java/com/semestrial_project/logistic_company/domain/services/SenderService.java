package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.sender.SenderResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderData;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface SenderService {

    SenderRs processSenderData(SenderData senderData, String departurePoint) throws DomainException;

    SenderRs updateSender(Long id, SenderData senderData, String departurePoint) throws DomainException;

    SenderResponse getSenderById(Long id) throws DomainException;

    List<SenderResponse> getAllSenders();
}

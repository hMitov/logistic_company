package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.office.CreateOffice;
import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.dto.office.UpdateOffice;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface OfficeService {

    OfficeResponse addNewOffice(CreateOffice createOffice) throws DomainException;

    OfficeResponse updateOffice(Long id, UpdateOffice updateOffice) throws DomainException;

    List<OfficeResponse> getAllOffices();

    OfficeResponse getOfficeById(Long id) throws DomainException;

    Office extractOfficeData(String officeAddress) throws DomainException;

    void removeOffice(Long officeId) throws DomainException;
}

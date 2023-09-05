package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeResponse;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface OfficeEmployeeService {

    OfficeEmployeeResponse createOfficeEmployee(OfficeEmployeeData officeEmployeeData) throws DomainException;

    OfficeEmployeeResponse updateOfficeEmployee(Long id, OfficeEmployeeData officeEmployeeData) throws DomainException;

    OfficeEmployeeResponse getOfficeEmployeeById(Long id) throws DomainException;

    List<OfficeEmployeeResponse> getAllOfficeEmployees();

    void removeOfficeEmployee(Long officeEmpId) throws DomainException;
}

package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierData;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierData supplierData) throws DomainException;
    SupplierResponse updateSupplier(Long id, SupplierData supplierData) throws DomainException;
    SupplierResponse getSupplierByEmployeeId(Long id) throws DomainException;
    SupplierResponse getSupplierById(Long id) throws DomainException;
    List<SupplierResponse> getAllSuppliers();
    void removeSupplier(Long id) throws DomainException;
}

package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

public interface AddressService {
    Address checkIfAddressExistsOrElseCreate(AddressRequest addressRequest);
    AddressResponse registerAddressToRecipient(Long id, AddressRequest addressRequest) throws DomainException;
    AddressResponse registerAddressToSender(Long id, AddressRequest addressRequest) throws DomainException;
    AddressResponse registerAddressToOffice(Long id, AddressRequest addressRequest) throws DomainException;
    AddressResponse updateOfficeAddress(Long id, AddressRequest addressRequest) throws DomainException;
    AddressResponse getRecipientActiveAddress(Long id) throws DomainException;
    AddressResponse getSenderActiveAddress(Long id) throws DomainException;
    AddressResponse getOfficeActiveAddress(Long id);
}

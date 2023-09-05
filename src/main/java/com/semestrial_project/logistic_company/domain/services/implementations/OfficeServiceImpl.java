package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.office.CreateOffice;
import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.dto.office.UpdateOffice;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.OfficeEmployee;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.AddressRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeEmployeeRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;
import static com.semestrial_project.logistic_company.domain.repository.specification.OfficeSpecification.withCityStreetStreetNum;
import static org.springframework.data.jpa.domain.Specification.where;

@AllArgsConstructor
@Service
public class OfficeServiceImpl implements OfficeService {

    private final AddressService addressService;

    private final AddressRepository addressRepository;

    private final OfficeRepository officeRepository;
    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final DomainAdapter domainAdapter;

    private final DomainValidator validator;

    @Transactional(rollbackFor = DomainException.class)
    public OfficeResponse addNewOffice(CreateOffice createOffice) throws DomainException {
        validator.validateOfficeOnCreate(createOffice);
        Office office = Office.builder()
                .dateOfEstablishment(createOffice.getDateOfEstablishment())
                .telephone(createOffice.getTelephone())
                .build();
        Office savedOffice = this.officeRepository.save(office);
        AddressResponse savedAddress = addressService.registerAddressToOffice(savedOffice.getId(), createOffice.getOfficeAddress());

        return domainAdapter.convertToOfficeResponse(getOffice(savedOffice.getId()), savedAddress);
    }

    @Transactional(rollbackFor = DomainException.class)
    public OfficeResponse updateOffice(Long id, UpdateOffice updateOffice) throws DomainException {
        Office officeLoaded = getOffice(id);
        validator.validateOfficeOnUpdate(updateOffice, officeLoaded);
        officeLoaded = officeLoaded.toBuilder()
                .telephone(Optional.ofNullable(updateOffice.getTelephone()).orElse(officeLoaded.getTelephone())).build();
        if(Objects.nonNull(updateOffice.getDateOfEstablishment())) {
            LocalDate localDate;
            try {
                localDate = LocalDate.parse(updateOffice.getDateOfEstablishment());
                localDate.format(DateTimeFormatter.ISO_DATE);
                officeLoaded = officeLoaded.toBuilder().dateOfEstablishment(localDate).build();
            } catch (DateTimeParseException e) {
                throw new DomainException(OFFICE_DATE_OF_ESTABLISHMENT_INVALID);
            }
        }
        Office savedOffice = this.officeRepository.save(officeLoaded);

        AddressResponse address;
        if(Objects.nonNull(updateOffice.getOfficeAddress())) {
            address = addressService.updateOfficeAddress(savedOffice.getId(), updateOffice.getOfficeAddress());
        } else {
            address = Optional.ofNullable(addressService.getOfficeActiveAddress(id)).orElse(new AddressResponse());
        }

        return domainAdapter.convertToOfficeResponse(savedOffice, address);
    }

    private Office getOffice(Long id) throws DomainException {
        return officeRepository.findById(id).orElseThrow(() -> new DomainException(OFFICE_NOT_FOUND));
    }

     public List<OfficeResponse> getAllOffices() {
        List<Office> offices = officeRepository.findAll();
        List<OfficeResponse> officeResponses = new ArrayList<>();
        for(Office office: offices) {
            AddressResponse addressResponse = Optional.ofNullable(addressService.getOfficeActiveAddress(office.getId()))
                    .orElse(new AddressResponse());
            officeResponses.add(domainAdapter.convertToOfficeResponse(office, addressResponse));
        }

        return officeResponses;
    }

    public OfficeResponse getOfficeById(Long id) throws DomainException {
        Office office = getOffice(id);
        AddressResponse address = Optional.ofNullable(addressService.getOfficeActiveAddress(id)).orElse(new AddressResponse());

        return domainAdapter.convertToOfficeResponse(office, address);
    }

    public Office extractOfficeData(String officeAddress) throws DomainException {
        String[] officeAddressInfo = officeAddress.split(", ");
        String city;
        String street;
        String streetNum;

        if (officeAddressInfo.length == 3) {
            city = officeAddressInfo[0];
            street = officeAddressInfo[1];
            streetNum = officeAddressInfo[2];

            List<Office> officeLoaded = officeRepository.findAll(where(withCityStreetStreetNum(city,street, streetNum)));
            if (officeLoaded.isEmpty()) {
                throw new DomainException(OFFICE_ADDRESS_INVALID);
            }

            return officeLoaded.get(0);
        } else {
            throw new DomainException(OFFICE_ADDRESS_DATA_INVALID);
        }
    }

    @Transactional
    public void removeOffice(Long officeId) throws DomainException {
        Office office = getOffice(officeId);
        for(Address address : office.getAddresses()) {
            address.setActiveOffice(false);
            address.setOffice(null);
            addressRepository.save(address);
        }

        for(OfficeEmployee officeEmployee : office.getOfficeEmployees()) {
            officeEmployee.setOffice(null);

            officeEmployeeRepository.save(officeEmployee);
        }

        officeRepository.delete(office);
    }
}

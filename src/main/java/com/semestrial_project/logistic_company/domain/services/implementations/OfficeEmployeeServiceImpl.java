package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeResponse;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.OfficeEmployee;
import com.semestrial_project.logistic_company.domain.entity.Supplier;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.OfficeEmployeeRepository;
import com.semestrial_project.logistic_company.domain.repository.SupplierRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.OfficeEmployeeService;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;

@Service
@AllArgsConstructor
public class OfficeEmployeeServiceImpl implements OfficeEmployeeService {

    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final SupplierRepository supplierRepository;

    private final DomainAdapter domainAdapter;

    private final OfficeService officeService;

    private final DomainValidator validator;

    private final AddressService addressService;
    private final Integer MAX_EMPLOYEE_ID = 9999;

    public OfficeEmployeeResponse createOfficeEmployee(OfficeEmployeeData officeEmployeeData) throws DomainException {
        Optional<OfficeEmployee> officeEmployeeLoaded = officeEmployeeRepository.findOfficeEmployeeByEgn(officeEmployeeData.getEgn());
        Optional<Supplier> supplierLoaded = supplierRepository.findSupplierByEgn(officeEmployeeData.getEgn());
        if (officeEmployeeLoaded.isPresent() || supplierLoaded.isPresent()) {
            throw new DomainException(EMPLOYEE_ALREADY_EXISTS);
        }
        validator.validateEmployeeOnCreate(officeEmployeeData);

        Office office = officeService.extractOfficeData(officeEmployeeData.getOfficeAddress());
        if(office.getDateOfEstablishment().isAfter(officeEmployeeData.getDateOfEmploy())) {
            throw new DomainException(OFFICE_EMPLOYEE_DATE_OF_ESTABLISHMENT_INVALID);
        }
        long value = new Random().nextInt(MAX_EMPLOYEE_ID);
        Long employeeId = Long.valueOf(value);

        OfficeEmployee officeEmployee = OfficeEmployee.builder()
                .egn(officeEmployeeData.getEgn())
                .firstName(officeEmployeeData.getFirstName())
                .middleName(officeEmployeeData.getMiddleName())
                .lastName(officeEmployeeData.getLastName())
                .employeeId(employeeId)
                .salary(officeEmployeeData.getSalary())
                .dateOfEmploy(officeEmployeeData.getDateOfEmploy())
                .currentEmployee(true)
                .office(office).build();

        AddressResponse addressResponse = addressService.getOfficeActiveAddress(office.getId());

        return domainAdapter.convertToOfficeEmployeeResponse(this.officeEmployeeRepository.save(officeEmployee), addressResponse);
    }

    public OfficeEmployeeResponse updateOfficeEmployee(Long id, OfficeEmployeeData officeEmployeeData) throws DomainException {
        OfficeEmployee officeEmployee = getOfficeEmployee(id);
        validator.validateEmployeeOnUpdate(officeEmployeeData, officeEmployee);

        Office office = officeService.extractOfficeData(officeEmployeeData.getOfficeAddress());
        Optional<Double> supplierLoaded = Optional.ofNullable(officeEmployeeData.getSalary());
        double salary = supplierLoaded.isPresent() && supplierLoaded.get() != 0 ? officeEmployeeData.getSalary() : officeEmployee.getSalary();

        officeEmployee = officeEmployee.toBuilder()
                .egn(Optional.ofNullable(officeEmployeeData.getEgn()).orElse(officeEmployee.getEgn()))
                .firstName(Optional.ofNullable(officeEmployeeData.getFirstName()).orElse(officeEmployee.getFirstName()))
                .middleName(Optional.ofNullable(officeEmployeeData.getMiddleName()).orElse(officeEmployee.getMiddleName()))
                .lastName(Optional.ofNullable(officeEmployeeData.getLastName()).orElse(officeEmployee.getLastName()))
                .salary(salary)
                .office(office).build();

        AddressResponse addressResponse = addressService.getOfficeActiveAddress(office.getId());

        return domainAdapter.convertToOfficeEmployeeResponse(this.officeEmployeeRepository.save(officeEmployee), addressResponse);
    }

    private OfficeEmployee getOfficeEmployee(Long id) throws DomainException {
        return officeEmployeeRepository.findById(id)
                .orElseThrow(() -> new DomainException(OFFICE_EMPLOYEE_NOT_FOUND));
    }

    public OfficeEmployeeResponse getOfficeEmployeeById(Long id) throws DomainException {
        OfficeEmployee officeEmployee = getEmployee(id);
        AddressResponse addressResponse = Optional.ofNullable(officeEmployee.getOffice()).isPresent() ?
                addressService.getOfficeActiveAddress(officeEmployee.getOffice().getId()) : new AddressResponse();

        return domainAdapter.convertToOfficeEmployeeResponse(officeEmployee, addressResponse);
    }

    private OfficeEmployee getEmployee(Long id) throws DomainException {
        return officeEmployeeRepository.findById(id)
                .orElseThrow(() -> new DomainException(OFFICE_EMPLOYEE_NOT_FOUND));
    }

    public List<OfficeEmployeeResponse> getAllOfficeEmployees() {
        List<OfficeEmployee> officeEmployees = this.officeEmployeeRepository.findAll();
        List<OfficeEmployeeResponse> officeEmployeeResponses = new ArrayList<>();
        if (officeEmployees.isEmpty()) {
            return List.of();
        }
        for (OfficeEmployee officeEmployee : officeEmployees) {
            AddressResponse addressResponse = Optional.ofNullable(officeEmployee.getOffice()).isPresent() ?
                    addressService.getOfficeActiveAddress(officeEmployee.getOffice().getId()) : null;
            officeEmployeeResponses.add(domainAdapter.convertToOfficeEmployeeResponse(officeEmployee, addressResponse));
        }
        return officeEmployeeResponses;
    }

    public void removeOfficeEmployee(Long officeEmpId) throws DomainException {
        OfficeEmployee officeEmployee = getOfficeEmployee(officeEmpId);

        officeEmployee = officeEmployee.toBuilder()
                .currentEmployee(false)
                .build();
        officeEmployeeRepository.save(officeEmployee);
    }
}

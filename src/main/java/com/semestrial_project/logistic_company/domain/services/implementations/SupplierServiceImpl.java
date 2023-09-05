package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierData;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.entity.DrivingCategory;
import com.semestrial_project.logistic_company.domain.entity.OfficeEmployee;
import com.semestrial_project.logistic_company.domain.entity.Supplier;
import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.OfficeEmployeeRepository;
import com.semestrial_project.logistic_company.domain.repository.SupplierRepository;
import com.semestrial_project.logistic_company.domain.services.DrivingCategoryService;
import com.semestrial_project.logistic_company.domain.services.SupplierService;
import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;

@Service
@AllArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final OfficeEmployeeRepository officeEmployeeRepository;

    private final DomainAdapter domainAdapter;

    private final VehicleService vehicleService;

    private final DrivingCategoryService drivingCategoryService;

    private final DomainValidator validator;
    private final Integer MAX_EMPLOYEE_ID = 9999;

    public SupplierResponse createSupplier(SupplierData supplierData) throws DomainException {
        Optional<Supplier> supplierLoaded = supplierRepository.findSupplierByEgn(supplierData.getEgn());
        Optional<OfficeEmployee> officeEmployeeLoaded = officeEmployeeRepository.findOfficeEmployeeByEgn(supplierData.getEgn());
        if (supplierLoaded.isPresent() || officeEmployeeLoaded.isPresent()) {
            throw new DomainException(EMPLOYEE_ALREADY_EXISTS);
        }
        validator.validateEmployeeOnCreate(supplierData);

        if (vehicleService.checkIfVehicleHasMaxSuppliers(supplierData.getVehicleId())) {
            throw new DomainException(VEHICLE_SUPPLIERS_REACHED_MAXIMUM);
        }

        long value = new Random().nextInt(MAX_EMPLOYEE_ID);
        Long employeeId = Long.valueOf(value);

        Vehicle vehicle = domainAdapter.convertToVehicle(vehicleService.getVehicleByVehicleId(supplierData.getVehicleId()));

        Set<DrivingCategory> drivingCategories = domainAdapter.convertToDrivingCategory(
                drivingCategoryService.extractDrivingCategories(supplierData.getDrivingLicenseCategories())
        );

        Supplier supplier = Supplier.builder()
                .egn(supplierData.getEgn())
                .firstName(supplierData.getFirstName())
                .middleName(supplierData.getMiddleName())
                .lastName(supplierData.getLastName())
                .employeeId(employeeId)
                .salary(supplierData.getSalary())
                .dateOfEmploy(supplierData.getDateOfEmploy())
                .vehicle(vehicle)
                .drivingLicenseCategories(drivingCategories)
                .currentEmployee(true)
                .build();

        return domainAdapter.convertToSupplierResponse(this.supplierRepository.save(supplier));
    }

    public SupplierResponse updateSupplier(Long id, SupplierData supplierData) throws DomainException {
        Supplier supplier = getSupplier(id);
        validator.validateEmployeeOnUpdate(supplierData, supplier);

        Optional<Double> supplierLoaded = Optional.ofNullable(supplierData.getSalary());
        double salary = supplierLoaded.isPresent() && supplierLoaded.get() != 0 ? supplierData.getSalary() : supplier.getSalary();

        Vehicle vehicle;
        if (Optional.ofNullable(supplierData.getVehicleId()).isPresent() && !Objects.equals(supplierData.getVehicleId(), supplier.getVehicle().getVehicleId())) {
            if (vehicleService.checkIfVehicleHasMaxSuppliers(supplierData.getVehicleId())) {
                throw new DomainException(VEHICLE_SUPPLIERS_REACHED_MAXIMUM);
            }
            vehicle = domainAdapter.convertToVehicle(vehicleService.getVehicleByVehicleId(supplierData.getVehicleId()));
        } else {
            vehicle = supplier.getVehicle();
        }

        Set<DrivingCategory> drivingCategories = null;
        if (Optional.ofNullable(supplierData.getDrivingLicenseCategories()).isPresent()) {
            drivingCategories = domainAdapter.convertToDrivingCategory(
                    drivingCategoryService.extractDrivingCategories(supplierData.getDrivingLicenseCategories())
            );
        }

        supplier = supplier.toBuilder()
                .egn(Optional.ofNullable(supplierData.getEgn()).orElse(supplier.getEgn()))
                .firstName(Optional.ofNullable(supplierData.getFirstName()).orElse(supplier.getFirstName()))
                .middleName(Optional.ofNullable(supplierData.getMiddleName()).orElse(supplier.getMiddleName()))
                .lastName(Optional.ofNullable(supplierData.getLastName()).orElse(supplier.getLastName()))
                .salary(salary)
                .vehicle(vehicle)
                .drivingLicenseCategories(Optional.ofNullable(drivingCategories).orElse(supplier.getDrivingLicenseCategories())).build();

        return domainAdapter.convertToSupplierResponse(this.supplierRepository.save(supplier));
    }


    private Supplier getSupplier(Long id) throws DomainException {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new DomainException(SUPPLIER_NOT_FOUND));
    }

    public SupplierResponse getSupplierByEmployeeId(Long id) throws DomainException {
        return domainAdapter.convertToSupplierResponse(supplierRepository.findByEmployeeId(id)
                .orElseThrow(() -> new DomainException(SUPPLIER_NOT_FOUND)));
    }

    public SupplierResponse getSupplierById(Long id) throws DomainException {
        return domainAdapter.convertToSupplierResponse(supplierRepository.findById(id)
                .orElseThrow(() -> new DomainException(SUPPLIER_NOT_FOUND)));
    }

    public List<SupplierResponse> getAllSuppliers() {
        List<Supplier> suppliersLoaded = this.supplierRepository.findAll();
        for (Supplier supplier : suppliersLoaded) {
            supplier.setDrivingLicenseCategories(domainAdapter.convertToDrivingCategory(
                    drivingCategoryService.getDrivingCategoryBySupplierId(supplier.getId())));
        }

        return suppliersLoaded.stream().map(domainAdapter::convertToSupplierResponse).collect(Collectors.toList());
    }

    public void removeSupplier(Long supplierId) throws DomainException {
        Supplier supplier = getSupplier(supplierId);

        supplier = supplier.toBuilder()
                .currentEmployee(false)
                .build();
        supplierRepository.save(supplier);
    }
}

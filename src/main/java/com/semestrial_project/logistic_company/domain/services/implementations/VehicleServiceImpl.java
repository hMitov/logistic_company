package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.vehicle.CreateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.UpdateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.domain.entity.Supplier;
import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.SupplierRepository;
import com.semestrial_project.logistic_company.domain.repository.VehicleRepository;
import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;
import static com.semestrial_project.logistic_company.domain.repository.specification.VehicleSpecification.withRegPlateNum;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SupplierRepository supplierRepository;
    private final DomainAdapter domainAdapter;

    private final DomainValidator validator;

    public VehicleResponse createVehicle(CreateVehicle vehicleData) throws DomainException {
        List<Vehicle> vehiclesLoaded = vehicleRepository.findAll(where(withRegPlateNum(vehicleData.getRegPlateNumber())));
        if (!vehiclesLoaded.isEmpty()) {
            throw new DomainException(VEHICLE_ALREADY_EXISTS);
        }
        validator.validateVehicleOnCreate(vehicleData);

        long value = new Random().nextInt(9999);
        Long vehicleId = Long.valueOf(value);

        Vehicle vehicle = Vehicle.builder()
                .brand(vehicleData.getBrand())
                .model(vehicleData.getModel())
                .regPlateNumber(vehicleData.getRegPlateNumber())
                .vehicleId(vehicleId).build();

        return domainAdapter.convertToVehicleResponse(this.vehicleRepository.save(vehicle));
    }

    public VehicleResponse updateVehicle(Long id, UpdateVehicle vehicleData) throws DomainException {
        Vehicle vehicle = getVehicle(id);
        validator.validateVehicleOnUpdate(vehicleData, vehicle);

        if (!Objects.equals(vehicle.getRegPlateNumber(), vehicleData.getRegPlateNumber())) {
            List<Vehicle> vehiclesLoaded = vehicleRepository.findAll(where(withRegPlateNum(vehicleData.getRegPlateNumber())));
            if (!vehiclesLoaded.isEmpty()) {
                throw new DomainException(VEHICLE_ALREADY_EXISTS);
            }
        }

        vehicle = vehicle.toBuilder()
                .brand(Optional.ofNullable(vehicleData.getBrand()).orElse(vehicle.getBrand()))
                .model(Optional.ofNullable(vehicleData.getModel()).orElse(vehicle.getModel()))
                .regPlateNumber(Optional.ofNullable(vehicleData.getRegPlateNumber()).orElse(vehicle.getRegPlateNumber()))
                .build();

        return domainAdapter.convertToVehicleResponse(this.vehicleRepository.save(vehicle));
    }

    private Vehicle getVehicle(Long id) throws DomainException {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new DomainException(VEHICLE_NOT_FOUND));
    }

    public VehicleResponse getVehicleById(Long id) throws DomainException {
        return domainAdapter.convertToVehicleResponse(vehicleRepository.findById(id)
                .orElseThrow(() -> new DomainException(VEHICLE_NOT_FOUND)));
    }

    public VehicleResponse getVehicleByVehicleId(Long id) throws DomainException {
        return domainAdapter.convertToVehicleResponse(vehicleRepository.findByVehicleId(id)
                .orElseThrow(() -> new DomainException(VEHICLE_NOT_FOUND)));
    }

    public List<VehicleResponse> getAllVehicles() {
        return this.vehicleRepository.findAll().stream().map(domainAdapter::convertToVehicleResponse).collect(Collectors.toList());
    }

    public boolean checkIfVehicleHasMaxSuppliers(Long id) throws DomainException {
        Vehicle vehicle = vehicleRepository.findByVehicleId(id).orElseThrow(() -> new DomainException(VEHICLE_NOT_FOUND));
        return vehicle.getSuppliers().size() >= 2;
    }

    @Transactional
    public void removeVehicle(Long vehicleId) throws DomainException {
        Vehicle vehicle = getVehicle(vehicleId);
        for(Supplier supplier : vehicle.getSuppliers()) {
            supplier.setVehicle(null);
            supplierRepository.save(supplier);
        }

        vehicleRepository.delete(vehicle);
    }
}

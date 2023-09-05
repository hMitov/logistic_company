package com.semestrial_project.logistic_company.domain.services;

import com.semestrial_project.logistic_company.domain.dto.vehicle.CreateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.UpdateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;

import java.util.List;

public interface VehicleService {

    VehicleResponse createVehicle(CreateVehicle vehicleData) throws DomainException;

    VehicleResponse updateVehicle(Long id, UpdateVehicle vehicleData) throws DomainException;

    VehicleResponse getVehicleById(Long id) throws DomainException;

    VehicleResponse getVehicleByVehicleId(Long id) throws DomainException;

    List<VehicleResponse> getAllVehicles();

    boolean checkIfVehicleHasMaxSuppliers(Long id) throws DomainException;

    void removeVehicle(Long id) throws DomainException;
}

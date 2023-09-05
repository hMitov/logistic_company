package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.vehicle.CreateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.UpdateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.domain.entity.Vehicle;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.SupplierRepository;
import com.semestrial_project.logistic_company.domain.repository.VehicleRepository;
import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.domain.services.implementations.VehicleServiceImpl;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleServiceTests {

    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String VEHICLE_BRAND = "Mercedes";
    private final String VEHICLE_MODEL = "s class";
    private final String VEHICLE_REG_PLATE_NUM_1 = "CB3456AA";
    private final String VEHICLE_REG_PLATE_NUM_2 = "A0192BB";
    private final Long VEHICLE_ID = 1L;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private VehicleRepository vehicleRepository;
    private SupplierRepository supplierRepository;
    private DomainAdapter domainAdapter;
    private DomainValidator validator;
    private VehicleService vehicleService;


    @BeforeEach
    public void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);
        validator = new DomainValidator();
        vehicleService = new VehicleServiceImpl(vehicleRepository, supplierRepository, domainAdapter, validator);

        vehicle1 = Vehicle.builder()
                .id(ID_1)
                .brand(VEHICLE_BRAND)
                .model(VEHICLE_MODEL)
                .regPlateNumber(VEHICLE_REG_PLATE_NUM_1)
                .vehicleId(VEHICLE_ID)
                .build();
        
        vehicle2 = vehicle1.toBuilder()
                .regPlateNumber(VEHICLE_REG_PLATE_NUM_2)
                .build();
    }

    @Test
    public void createVehicle_VEHICLE_ALREADY_EXISTS__ERROR() {
        CreateVehicle data = getVehicleData();
        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of(vehicle1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.createVehicle(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_ALREADY_EXISTS);
    }

    @Test
    public void createVehicle_VEHICLE_BRAND_REQUIRED__ERROR() {
        CreateVehicle data = getVehicleData();
        data.setBrand(null);

        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.createVehicle(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_BRAND_REQUIRED);
    }

    @Test
    public void createVehicle_VEHICLE_MODEL_REQUIRED__ERROR() {
        CreateVehicle data = getVehicleData();
        data.setModel(null);

        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.createVehicle(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_MODEL_REQUIRED);
    }

    @Test
    public void createVehicle_VEHICLE_REGISTRATION_PLATE_REQUIRED__ERROR() {
        CreateVehicle data = getVehicleData();
        data.setRegPlateNumber(null);

        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.createVehicle(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_REGISTRATION_PLATE_REQUIRED);
    }

    @Test
    public void createVehicle_OK() throws DomainException {
        CreateVehicle data = getVehicleData();

        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle1);

        VehicleResponse response = vehicleService.createVehicle(data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getBrand(), data.getBrand());
        assertEquals(response.getModel(), data.getModel());
        assertEquals(response.getRegPlateNumber(), data.getRegPlateNumber());
    }

    @Test
    public void updateVehicle_VEHICLE_NOT_FOUND__ERROR() {
        UpdateVehicle data = getUpdateVehicleData();

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.updateVehicle(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_NOT_FOUND);
    }

    @Test
    public void updateVehicle_VEHICLE_BRAND_BLANK__ERROR() {
        UpdateVehicle data = getUpdateVehicleData();
        data.setBrand("");

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.updateVehicle(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_BRAND_BLANK);
    }

    @Test
    public void updateVehicle_VEHICLE_MODEL_BLANK__ERROR() {
        UpdateVehicle data = getUpdateVehicleData();
        data.setModel("");

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.updateVehicle(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_MODEL_BLANK);
    }

    @Test
    public void updateVehicle_VEHICLE_REGISTRATION_PLATE_BLANK__ERROR() {
        UpdateVehicle data = getUpdateVehicleData();
        data.setRegPlateNumber("");

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.updateVehicle(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_REGISTRATION_PLATE_BLANK);
    }

    @Test
    public void updateVehicle_VEHICLE_ALREADY_EXISTS__ERROR() {
        UpdateVehicle data = getUpdateVehicleData();

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of(vehicle1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    vehicleService.updateVehicle(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_ALREADY_EXISTS);
    }

    @Test
    public void updateVehicle_OK() throws DomainException {
        UpdateVehicle data = getUpdateVehicleData();
        data.setRegPlateNumber(VEHICLE_REG_PLATE_NUM_2);

        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(vehicleRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle2);
        
        VehicleResponse response = vehicleService.updateVehicle(ID_1, data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getBrand(), data.getBrand());
        assertEquals(response.getModel(), data.getModel());
        assertEquals(response.getRegPlateNumber(), data.getRegPlateNumber());
    }

    @Test
    public void getVehicleById_OK() throws DomainException {
        when(vehicleRepository.findById(any(Long.class))).thenReturn(Optional.of(vehicle1));

        VehicleResponse response = vehicleService.getVehicleById(ID_1);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getBrand(), vehicle1.getBrand());
        assertEquals(response.getModel(), vehicle1.getModel());
        assertEquals(response.getRegPlateNumber(), vehicle1.getRegPlateNumber());
        assertEquals(response.getVehicleId(), vehicle1.getVehicleId());
    }

    @Test
    public void getVehicleByVehicleId_OK() throws DomainException {
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle1));

        VehicleResponse response = vehicleService.getVehicleByVehicleId(ID_1);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getBrand(), vehicle1.getBrand());
        assertEquals(response.getModel(), vehicle1.getModel());
        assertEquals(response.getRegPlateNumber(), vehicle1.getRegPlateNumber());
        assertEquals(response.getVehicleId(), vehicle1.getVehicleId());
    }

    @Test
    public void getAllVehicles_OK() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle1, vehicle2));
        List<VehicleResponse> responses = vehicleService.getAllVehicles();
        assertEquals(responses.size(), 2);
    }

    private CreateVehicle getVehicleData() {
        CreateVehicle data = new CreateVehicle();
        data.setBrand(VEHICLE_BRAND);
        data.setModel(VEHICLE_MODEL);
        data.setRegPlateNumber(VEHICLE_REG_PLATE_NUM_1);

        return data;
    }

    private UpdateVehicle getUpdateVehicleData() {
        UpdateVehicle data = new UpdateVehicle();
        data.setBrand(VEHICLE_BRAND);
        data.setModel(VEHICLE_MODEL);
        data.setRegPlateNumber(VEHICLE_REG_PLATE_NUM_2);

        return data;
    }

}

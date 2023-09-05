package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierData;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.entity.*;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.DrivingCategoryRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeEmployeeRepository;
import com.semestrial_project.logistic_company.domain.repository.SupplierRepository;
import com.semestrial_project.logistic_company.domain.repository.VehicleRepository;
import com.semestrial_project.logistic_company.domain.services.DrivingCategoryService;
import com.semestrial_project.logistic_company.domain.services.SupplierService;
import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.domain.services.implementations.DrivingCategoryServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.SupplierServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.VehicleServiceImpl;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SupplierServiceTests {

    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String VEHICLE_BRAND = "Mercedes";
    private final String VEHICLE_MODEL = "s class";
    private final String VEHICLE_REG_PLATE_NUM_1 = "CB3456AA";
    private final String VEHICLE_REG_PLATE_NUM_2 = "A0192BB";
    private final Long VEHICLE_ID = 1L;
    private final String EGN = "9987654321";
    private final String FIRST_NAME = "Martin";
    private final String MIDDLE_NAME_1 = "Ivanov";
    private final String MIDDLE_NAME_2 = "Georgiev";
    private final String LAST_NAME = "Stoyanov";
    private final String TELEPHONE = "0876256258";
    private final Double SALARY = 40000D;
    private final Long VEHICLE_ID_1 = 123L;
    private final Long VEHICLE_ID_2 = 145L;
    private final Long EMPLOYEE_ID = 1L;
    private Supplier supplier1;
    private Supplier supplier2;
    private OfficeEmployee officeEmployee1;
    private Vehicle vehicle1;
    private Vehicle vehicle2;
    private Vehicle vehicle3;
    private Office office;
    private DrivingCategory drivingCategory1;
    private Set<DrivingCategory> drivingCategories;
    private final Set<String> DRIVING_CATEGORIES = Set.of("A");
    private SupplierRepository supplierRepository;
    private OfficeEmployeeRepository officeEmployeeRepository;
    private VehicleRepository vehicleRepository;
    private DrivingCategoryRepository drivingCategoryRepository;
    private DomainAdapter domainAdapter;
    private DomainValidator validator;
    private VehicleService vehicleService;
    private DrivingCategoryService drivingCategoryService;
    private SupplierService supplierService;

    @BeforeEach
    public void setUp() {
        supplierRepository = mock(SupplierRepository.class);
        officeEmployeeRepository = mock(OfficeEmployeeRepository.class);
        vehicleRepository = mock(VehicleRepository.class);
        drivingCategoryRepository = mock(DrivingCategoryRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);
        validator = new DomainValidator();
        vehicleService = new VehicleServiceImpl(vehicleRepository, supplierRepository, domainAdapter, validator);
        drivingCategoryService = new DrivingCategoryServiceImpl(drivingCategoryRepository, domainAdapter);
        supplierService = new SupplierServiceImpl(
                supplierRepository,
                officeEmployeeRepository,
                domainAdapter,
                vehicleService,
                drivingCategoryService,
                validator);

        drivingCategory1 = DrivingCategory.builder()
                .id(ID_1)
                .categoryName("A")
                .build();

        drivingCategories = Set.of(drivingCategory1);

        vehicle3 = Vehicle.builder()
                .id(ID_1)
                .brand(VEHICLE_BRAND)
                .model(VEHICLE_MODEL)
                .regPlateNumber(VEHICLE_REG_PLATE_NUM_1)
                .vehicleId(VEHICLE_ID_1)
                .build();

        supplier1 = Supplier.builder()
                .id(ID_1)
                .egn(EGN)
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME_1)
                .lastName(LAST_NAME)
                .employeeId(EMPLOYEE_ID)
                .salary(SALARY)
                .vehicle(vehicle3)
                .dateOfEmploy(LocalDate.now())
                .drivingLicenseCategories(drivingCategories)
                .build();

        supplier2 = supplier1.toBuilder()
                .middleName(MIDDLE_NAME_2)
                .build();

        vehicle1 = vehicle3.toBuilder()
                .suppliers(Set.of(supplier1))
                .build();

        vehicle2 = vehicle1.toBuilder()
                .regPlateNumber(VEHICLE_REG_PLATE_NUM_2)
                .vehicleId(VEHICLE_ID_2)
                .suppliers(Set.of(supplier1, supplier2))
                .build();


        office = Office.builder()
                .id(ID_1)
                .dateOfEstablishment(LocalDate.now())
                .telephone(TELEPHONE)
                .build();

        officeEmployee1 = OfficeEmployee.builder()
                .id(ID_1)
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME_1)
                .lastName(LAST_NAME)
                .employeeId(EMPLOYEE_ID)
                .salary(SALARY)
                .dateOfEmploy(LocalDate.now())
                .office(office)
                .build();
    }

    @Test
    public void createSupplier_SUPPLIER_ALREADY_EXISTS__ERROR() {
        SupplierData data = getSupplierData();
        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.of(supplier1));
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_ALREADY_EXISTS);
    }

    @Test
    public void createSupplier_OFFICE_EMPLOYEE_ALREADY_EXISTS__ERROR() {
        SupplierData data = getSupplierData();
        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.of(officeEmployee1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_ALREADY_EXISTS);
    }

    @Test
    public void createSupplier_SUPPLIER_EGN_REQUIRED__ERROR() {
        SupplierData data = getSupplierData();
        data.setEgn(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_EGN_REQUIRED);
    }

    @Test
    public void createSupplier_SUPPLIER_FIRST_NAME_REQUIRED__ERROR() {
        SupplierData data = getSupplierData();
        data.setFirstName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_FIRST_NAME_REQUIRED);
    }

    @Test
    public void createSupplier_SUPPLIER_MIDDLE_NAME_REQUIRED__ERROR() {
        SupplierData data = getSupplierData();
        data.setMiddleName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_MIDDLE_NAME_REQUIRED);
    }

    @Test
    public void createSupplier_SUPPLIER_LAST_NAME_REQUIRED__ERROR() {
        SupplierData data = getSupplierData();
        data.setLastName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_LAST_NAME_REQUIRED);
    }

    @Test
    public void createSupplier_SUPPLIER_SALARY_REQUIRED__ERROR() {
        SupplierData data = getSupplierData();
        data.setSalary(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_SALARY_REQUIRED);
    }

    @Test
    public void createSupplier_VEHICLE_NOT_FOUND__ERROR() {
        SupplierData data = getSupplierData();

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_NOT_FOUND);
    }

    @Test
    public void createSupplier_VEHICLE_SUPPLIERS_REACHED_MAXIMUM__ERROR() {
        SupplierData data = getSupplierData();

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle2));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_SUPPLIERS_REACHED_MAXIMUM);
    }

    @Test
    public void createSupplier_DRIVING_CATEGORY_NOT_FOUND__ERROR() {
        SupplierData data = getSupplierData();

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(drivingCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.createSupplier(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.DRIVING_CATEGORY_NOT_FOUND);
    }

    @Test
    public void createSupplier_OK() throws DomainException {
        SupplierData data = getSupplierData();

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(drivingCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(drivingCategory1));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier1);
        SupplierResponse response = supplierService.createSupplier(data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), data.getEgn());
        assertEquals(response.getFirstName(), data.getFirstName());
        assertEquals(response.getMiddleName(), data.getMiddleName());
        assertEquals(response.getLastName(), data.getLastName());
        assertEquals(response.getSalary(), data.getSalary());
    }

    private SupplierData getSupplierData() {
        SupplierData data = new SupplierData();
        data.setEgn(EGN);
        data.setFirstName(FIRST_NAME);
        data.setMiddleName(MIDDLE_NAME_1);
        data.setLastName(LAST_NAME);
        data.setSalary(SALARY);
        data.setDateOfEmploy(LocalDate.now());
        data.setDrivingLicenseCategories(DRIVING_CATEGORIES);
        data.setVehicleId(VEHICLE_ID_1);
        
        return data;
    }

    @Test
    public void updateSupplier_SUPPLIER_NOT_FOUND__ERROR() {
        SupplierData data = getUpdateSupplierData();

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.SUPPLIER_NOT_FOUND);
    }

    @Test
    public void updateSupplier_SUPPLIER_EGN_BLANK__ERROR() {
        SupplierData data = getUpdateSupplierData();
        data.setEgn("");

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_EGN_BLANK);
    }

    @Test
    public void updateSupplier_SUPPLIER_FIRST_NAME_BLANK__ERROR() {
        SupplierData data = getUpdateSupplierData();
        data.setFirstName("");

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_FIRST_NAME_BLANK);
    }

    @Test
    public void updateSupplier_SUPPLIER_MIDDLE_NAME_BLANK__ERROR() {
        SupplierData data = getUpdateSupplierData();
        data.setMiddleName("");

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_MIDDLE_NAME_BLANK);
    }

    @Test
    public void updateSupplier_SUPPLIER_LAST_NAME_BLANK__ERROR() {
        SupplierData data = getUpdateSupplierData();
        data.setLastName("");

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_LAST_NAME_BLANK);
    }

    @Test
    public void updateSupplier_VEHICLE_NOT_FOUND__ERROR() {
        SupplierData data = getUpdateSupplierData();

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_NOT_FOUND);
    }

    @Test
    public void updateSupplier_VEHICLE_SUPPLIERS_REACHED_MAXIMUM__ERROR() {
        SupplierData data = getUpdateSupplierData();

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle2));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.VEHICLE_SUPPLIERS_REACHED_MAXIMUM);
    }

    @Test
    public void updateSupplier_DRIVING_CATEGORY_NOT_FOUND__ERROR() {
        SupplierData data = getUpdateSupplierData();

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(drivingCategoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    supplierService.updateSupplier(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.DRIVING_CATEGORY_NOT_FOUND);
    }

    @Test
    public void updateSupplier_OK() throws DomainException {
        SupplierData data = getUpdateSupplierData();

        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));
        when(vehicleRepository.findByVehicleId(any(Long.class))).thenReturn(Optional.of(vehicle1));
        when(drivingCategoryRepository.findById(any(Long.class))).thenReturn(Optional.of(drivingCategory1));
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier2);

        SupplierResponse response = supplierService.updateSupplier(ID_1, data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), data.getEgn());
        assertEquals(response.getFirstName(), data.getFirstName());
        assertEquals(response.getMiddleName(), data.getMiddleName());
        assertEquals(response.getLastName(), data.getLastName());
        assertEquals(response.getSalary(), data.getSalary());
    }

    @Test
    public void getSupplierByEmployeeId_OK() throws DomainException {
        when(supplierRepository.findByEmployeeId(any(Long.class))).thenReturn(Optional.of(supplier1));

        SupplierResponse response = supplierService.getSupplierByEmployeeId(ID_1);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), supplier1.getEgn());
        assertEquals(response.getFirstName(), supplier1.getFirstName());
        assertEquals(response.getMiddleName(), supplier1.getMiddleName());
        assertEquals(response.getLastName(), supplier1.getLastName());
        assertEquals(response.getEmployeeId(), supplier1.getEmployeeId());
        assertEquals(response.getSalary(), supplier1.getSalary());
        assertEquals(response.getDateOfEmploy(), supplier1.getDateOfEmploy());
        assertThat(response.getDrivingLicenseCategories()).usingRecursiveComparison().isEqualTo(supplier1.getDrivingLicenseCategories());
        assertEquals(response.getVehicle().getVehicleId(), supplier1.getVehicle().getVehicleId());
    }

    @Test
    public void getSupplierById_OK() throws DomainException {
        when(supplierRepository.findById(any(Long.class))).thenReturn(Optional.of(supplier1));

        SupplierResponse response = supplierService.getSupplierById(ID_1);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), supplier1.getEgn());
        assertEquals(response.getFirstName(), supplier1.getFirstName());
        assertEquals(response.getMiddleName(), supplier1.getMiddleName());
        assertEquals(response.getLastName(), supplier1.getLastName());
        assertEquals(response.getEmployeeId(), supplier1.getEmployeeId());
        assertEquals(response.getSalary(), supplier1.getSalary());
        assertEquals(response.getDateOfEmploy(), supplier1.getDateOfEmploy());
        assertThat(response.getDrivingLicenseCategories()).usingRecursiveComparison().isEqualTo(supplier1.getDrivingLicenseCategories());
        assertEquals(response.getVehicle().getVehicleId(), supplier1.getVehicle().getVehicleId());
    }

    @Test
    public void getAllSuppliers_OK() {
        when(supplierRepository.findAll()).thenReturn(List.of(supplier1, supplier2));

        List<SupplierResponse> response = supplierService.getAllSuppliers();

        assertEquals(response.size(), 2);
    }

    private SupplierData getUpdateSupplierData() {
        SupplierData data = new SupplierData();
        data.setEgn(EGN);
        data.setFirstName(FIRST_NAME);
        data.setMiddleName(MIDDLE_NAME_2);
        data.setLastName(LAST_NAME);
        data.setSalary(SALARY);
        data.setDrivingLicenseCategories(DRIVING_CATEGORIES);
        data.setVehicleId(VEHICLE_ID_2);

        return data;
    }

}

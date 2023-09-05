package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeResponse;
import com.semestrial_project.logistic_company.domain.entity.*;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.*;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.OfficeEmployeeService;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.implementations.AddressServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.OfficeEmployeeServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.OfficeServiceImpl;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;

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

public class OfficeEmployeeServiceTests {

    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String VEHICLE_BRAND = "Mercedes";
    private final String VEHICLE_MODEL = "s class";
    private final String VEHICLE_REG_PLATE_NUM_1 = "CB3456AA";
    private final String VEHICLE_REG_PLATE_NUM_2 = "A0192BB";
    private final Long VEHICLE_ID_1 = 1L;
    private final String EGN = "9987654321";
    private final String FIRST_NAME = "Martin";
    private final String MIDDLE_NAME_1 = "Ivanov";
    private final String MIDDLE_NAME_2 = "Georgiev";
    private final String LAST_NAME = "Stoyanov";
    private final String TELEPHONE = "0876256258";
    private final Double SALARY = 40000D;
    private final Long EMPLOYEE_ID = 1L;
    private OfficeEmployee officeEmployee1;
    private OfficeEmployee officeEmployee2;
    private Office office;
    private Vehicle vehicle3;
    private Supplier supplier1;
    private DrivingCategory drivingCategory1;
    private Set<DrivingCategory> drivingCategories;
    private OfficeEmployeeRepository officeEmployeeRepository;
    private SupplierRepository supplierRepository;
    private AddressRepository addressRepository;
    private RecipientRepository recipientRepository;
    private SenderRepository senderRepository;
    private OfficeRepository officeRepository;
    private DomainAdapter domainAdapter;
    private OfficeService officeService;
    private DomainValidator validator;
    private AddressService addressService;
    private OfficeEmployeeService officeEmployeeService;


    @BeforeEach
    public void setUp() {
        supplierRepository = mock(SupplierRepository.class);
        officeEmployeeRepository = mock(OfficeEmployeeRepository.class);
        addressRepository = mock(AddressRepository.class);
        recipientRepository = mock(RecipientRepository.class);
        senderRepository = mock(SenderRepository.class);
        officeRepository = mock(OfficeRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);
        validator = new DomainValidator();

        addressService = new AddressServiceImpl(
                addressRepository,
                recipientRepository,
                senderRepository,
                officeRepository,
                domainAdapter);

        officeService = new OfficeServiceImpl(
                addressService,
                addressRepository,
                officeRepository,
                officeEmployeeRepository,
                domainAdapter,
                validator);

        officeEmployeeService = new OfficeEmployeeServiceImpl(
                officeEmployeeRepository,
                supplierRepository,
                domainAdapter,
                officeService,
                validator,
                addressService
        );

        office = Office.builder()
                .id(ID_1)
                .dateOfEstablishment(LocalDate.now())
                .telephone(TELEPHONE)
                .build();

        officeEmployee1 = OfficeEmployee.builder()
                .id(ID_1)
                .egn(EGN)
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME_1)
                .lastName(LAST_NAME)
                .employeeId(EMPLOYEE_ID)
                .salary(SALARY)
                .dateOfEmploy(LocalDate.now())
                .office(office)
                .build();

        officeEmployee2 = officeEmployee1.toBuilder()
                .middleName(MIDDLE_NAME_2)
                .office(office)
                .build();

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

    }

    @Test
    public void createOfficeEmployee_SUPPLIER_ALREADY_EXISTS__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.of(supplier1));
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_ALREADY_EXISTS);
    }

    @Test
    public void createOfficeEmployee_OFFICE_EMPLOYEE_ALREADY_EXISTS__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.of(officeEmployee1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_ALREADY_EXISTS);
    }

    @Test
    public void createSupplier_OFFICE_EMPLOYEE_EGN_REQUIRED__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        data.setEgn(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_EGN_REQUIRED);
    }

    @Test
    public void createOfficeEmployee_OFFICE_EMPLOYEE_FIRST_NAME_REQUIRED__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        data.setFirstName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_FIRST_NAME_REQUIRED);
    }

    @Test
    public void createOfficeEmployee_OFFICE_EMPLOYEE_MIDDLE_NAME_REQUIRED__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        data.setMiddleName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_MIDDLE_NAME_REQUIRED);
    }

    @Test
    public void createOfficeEmployee_OFFICE_EMPLOYEE_LAST_NAME_REQUIRED__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        data.setLastName(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_LAST_NAME_REQUIRED);
    }

    @Test
    public void createOfficeEmployee_OFFICE_EMPLOYEE_SALARY_REQUIRED__ERROR() {
        OfficeEmployeeData data = getOfficeEmployeeData();
        data.setSalary(null);

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.createOfficeEmployee(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_SALARY_REQUIRED);
    }

    @Test
    public void createOfficeEmployee_OK() throws DomainException {
        OfficeEmployeeData data = getOfficeEmployeeData();

        when(supplierRepository.findSupplierByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeEmployeeRepository.findOfficeEmployeeByEgn(any(String.class))).thenReturn(Optional.empty());
        when(officeRepository.findAll(any(Specification.class))).thenReturn(List.of(office, office));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(officeEmployeeRepository.save(any(OfficeEmployee.class))).thenReturn(officeEmployee1);
        OfficeEmployeeResponse response = officeEmployeeService.createOfficeEmployee(data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), data.getEgn());
        assertEquals(response.getFirstName(), data.getFirstName());
        assertEquals(response.getMiddleName(), data.getMiddleName());
        assertEquals(response.getLastName(), data.getLastName());
        assertEquals(response.getSalary(), data.getSalary());
    }

    private OfficeEmployeeData getOfficeEmployeeData() {
        OfficeEmployeeData data = new OfficeEmployeeData();
        data.setEgn(EGN);
        data.setFirstName(FIRST_NAME);
        data.setMiddleName(MIDDLE_NAME_1);
        data.setLastName(LAST_NAME);
        data.setSalary(SALARY);
        data.setDateOfEmploy(LocalDate.now());
        data.setOfficeAddress("Sofia, Priroda, 21");

        return data;
    }

    @Test
    public void updateOfficeEmployee_SUP_NOT_FOUND__ERROR() {
        OfficeEmployeeData data = getUpdateOfficeEmployeeData();

        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.updateOfficeEmployee(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_EMPLOYEE_NOT_FOUND);
    }

    @Test
    public void updateOfficeEmployee_OFFICE_EMPLOYEE_EGN_BLANK__ERROR() {
        OfficeEmployeeData data = getUpdateOfficeEmployeeData();
        data.setEgn("");

        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.of(officeEmployee1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.updateOfficeEmployee(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_EGN_BLANK);
    }

    @Test
    public void updateOfficeEmployee_OFFICE_EMPLOYEE_FIRST_NAME_BLANK__ERROR() {
        OfficeEmployeeData data = getUpdateOfficeEmployeeData();
        data.setFirstName("");

        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.of(officeEmployee1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.updateOfficeEmployee(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_FIRST_NAME_BLANK);
    }

    @Test
    public void updateOfficeEmployee_OFFICE_EMPLOYEE_MIDDLE_NAME_BLANK__ERROR() {
        OfficeEmployeeData data = getUpdateOfficeEmployeeData();
        data.setMiddleName("");

        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.of(officeEmployee1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeEmployeeService.updateOfficeEmployee(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMPLOYEE_MIDDLE_NAME_BLANK);
    }

    @Test
    public void updateOfficeEmployee_OK() throws DomainException {
        OfficeEmployeeData data = getUpdateOfficeEmployeeData();

        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.of(officeEmployee1));
        when(officeRepository.findAll(any(Specification.class))).thenReturn(List.of(office, office));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(officeEmployeeRepository.save(any(OfficeEmployee.class))).thenReturn(officeEmployee2);
        OfficeEmployeeResponse response = officeEmployeeService.updateOfficeEmployee(ID_1, data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), data.getEgn());
        assertEquals(response.getFirstName(), data.getFirstName());
        assertEquals(response.getMiddleName(), data.getMiddleName());
        assertEquals(response.getLastName(), data.getLastName());
        assertEquals(response.getSalary(), data.getSalary());
    }

    @Test
    public void getOfficeEmployeeById_OK() throws DomainException {
        when(officeEmployeeRepository.findById(any(Long.class))).thenReturn(Optional.of(officeEmployee1));

        OfficeEmployeeResponse response = officeEmployeeService.getOfficeEmployeeById(ID_1);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getEgn(), officeEmployee1.getEgn());
        assertEquals(response.getFirstName(), officeEmployee1.getFirstName());
        assertEquals(response.getMiddleName(), officeEmployee1.getMiddleName());
        assertEquals(response.getLastName(), officeEmployee1.getLastName());
        assertEquals(response.getEmployeeId(), officeEmployee1.getEmployeeId());
        assertEquals(response.getSalary(), officeEmployee1.getSalary());
        assertEquals(response.getDateOfEmploy(), officeEmployee1.getDateOfEmploy());
        assertThat(response.getOffice()).usingRecursiveComparison().isEqualTo(response.getOffice());
    }

    @Test
    public void getAllOfficeEmployees_OK() {
        when(officeEmployeeRepository.findAll()).thenReturn(List.of(officeEmployee1, officeEmployee2));

        List<OfficeEmployeeResponse> response = officeEmployeeService.getAllOfficeEmployees();

        assertEquals(response.size(), 2);
    }


    private OfficeEmployeeData getUpdateOfficeEmployeeData() {
        OfficeEmployeeData data = new OfficeEmployeeData();
        data.setEgn(EGN);
        data.setFirstName(FIRST_NAME);
        data.setMiddleName(MIDDLE_NAME_2);
        data.setLastName(LAST_NAME);
        data.setSalary(SALARY);
        data.setDateOfEmploy(LocalDate.now());
        data.setOfficeAddress("Sofia, Priroda, 21");

        return data;
    }
}

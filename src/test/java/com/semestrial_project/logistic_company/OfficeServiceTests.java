package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.office.CreateOffice;
import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.dto.office.UpdateOffice;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.OfficeEmployee;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.*;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.domain.services.implementations.AddressServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.OfficeServiceImpl;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OfficeServiceTests {
    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String TELEPHONE_1 = "0876256258";
    private final String TELEPHONE_2 = "0888253458";
    private final String CITY = "Sofia";
    private final String STREET = "Cherkovna";
    private final String STREET_NUM = "21";
    private final String POST_CODE = "1680";
    private Office office1;
    private Office office2;
    private Address address1;
    private AddressService addressService;
    private OfficeRepository officeRepository;
    private DomainAdapter domainAdapter;
    private DomainValidator validator;
    private OfficeService officeService;
    private AddressRepository addressRepository;
    private RecipientRepository recipientRepository;
    private SenderRepository senderRepository;
    private OfficeEmployeeRepository officeEmployeeRepository;


    @BeforeEach
    public void setUp() {
        officeRepository = mock(OfficeRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);
        validator = new DomainValidator();
        addressRepository = mock(AddressRepository.class);
        recipientRepository = mock(RecipientRepository.class);
        senderRepository = mock(SenderRepository.class);
        officeEmployeeRepository = mock(OfficeEmployeeRepository.class);

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

        office1 = Office.builder()
                .id(ID_1)
                .dateOfEstablishment(LocalDate.now())
                .telephone(TELEPHONE_1)
                .build();

        office2 = Office.builder()
                .id(ID_1)
                .dateOfEstablishment(LocalDate.now())
                .telephone(TELEPHONE_2)
                .build();

        address1 = Address.builder()
                .id(ID_1)
                .city(CITY)
                .street(STREET)
                .streetNum(STREET_NUM)
                .postCode(POST_CODE)
                .office(office1)
                .activeOffice(true)
                .build();
    }

    @Test
    public void addOffice_ADDRESS_CITY_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.getOfficeAddress().setCity(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_CITY_REQUIRED);
    }

    @Test
    public void addOffice_ADDRESS_POST_CODE_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.getOfficeAddress().setPostCode(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_POST_CODE_REQUIRED);
    }

    @Test
    public void addOffice_ADDRESS_STREET_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.getOfficeAddress().setStreet(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_REQUIRED);
    }

    @Test
    public void addOffice_ADDRESS_STREET_NUMBER_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.getOfficeAddress().setStreetNum(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_NUMBER_REQUIRED);
    }

    @Test
    public void addOffice_OFFICE_TELEPHONE_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.setTelephone(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_TELEPHONE_REQUIRED);
    }

    @Test
    public void addOffice_OFFICE_DATE_OF_ESTABLISHMENT_REQUIRED__ERROR() {
        CreateOffice data = getCreateOffice();
        data.setDateOfEstablishment(null);

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_DATE_OF_ESTABLISHMENT_REQUIRED);
    }

    @Test
    public void addOffice_OFFICE_NOT_FOUND__ERROR() {
        CreateOffice data = getCreateOffice();

        when(officeRepository.save(any(Office.class))).thenReturn(office1);
        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.addNewOffice(data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_NOT_FOUND);
    }

    @Test
    public void addOffice_OK() throws DomainException {
        CreateOffice data = getCreateOffice();

        when(officeRepository.save(any(Office.class))).thenReturn(office1);
        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address1);
        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));

        OfficeResponse response = officeService.addNewOffice(data);

        assertEquals(response.getId(), ID_1);
        assertThat(response.getOfficeAddress()).usingRecursiveComparison().isEqualTo(address1);
        assertEquals(response.getTelephone(), office1.getTelephone());
        assertEquals(response.getDateOfEstablishment(), office1.getDateOfEstablishment());
    }

    private CreateOffice getCreateOffice() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        CreateOffice data = new CreateOffice();
        data.setOfficeAddress(addressRequest);
        data.setTelephone(TELEPHONE_1);
        data.setDateOfEstablishment(LocalDate.now());

        return data;
    }

    @Test
    public void updateOffice_OFFICE_NOT_FOUND__ERROR() {
        UpdateOffice data = getUpdateOffice();
        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.updateOffice(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_NOT_FOUND);
    }

    @Test
    public void updateOffice_OFFICE_TELEPHONE_BLANK__ERROR() {
        UpdateOffice data = getUpdateOffice();
        data.setOfficeAddress(null);
        data.setTelephone("");

        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.updateOffice(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_TELEPHONE_BLANK);
    }

    @Test
    public void updateOffice_OK() throws DomainException {
        UpdateOffice data = getUpdateOffice();
        data.setOfficeAddress(null);

        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));
        when(officeRepository.save(any(Office.class))).thenReturn(office2);
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(officeRepository.save(any(Office.class))).thenReturn(office2);

        OfficeResponse response = officeService.updateOffice(ID_1, data);

        assertEquals(response.getId(), ID_1);
        assertThat(response.getOfficeAddress()).usingRecursiveComparison().isEqualTo(new Address());
        assertEquals(response.getTelephone(), data.getTelephone());
        assertEquals(response.getDateOfEstablishment(), data.getDateOfEstablishment());
    }

    @Test
    public void getOfficeById_OK() throws DomainException {
        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address1));

        OfficeResponse response = officeService.getOfficeById(ID_1);

        assertEquals(response.getId(), ID_1);
        assertThat(response.getOfficeAddress()).usingRecursiveComparison().isEqualTo(address1);
        assertEquals(response.getTelephone(), office1.getTelephone());
        assertEquals(response.getDateOfEstablishment(), office1.getDateOfEstablishment());
    }

    @Test
    public void getAllOffices_OK() {
        when(officeRepository.findAll()).thenReturn(List.of(office1, office2));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address1));

        List<OfficeResponse> response = officeService.getAllOffices();

        assertEquals(response.size(), 2);
    }



    private UpdateOffice getUpdateOffice() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        UpdateOffice data = new UpdateOffice();
        data.setOfficeAddress(addressRequest);
        data.setTelephone(TELEPHONE_2);
        data.setDateOfEstablishment(LocalDate.now());

        return data;
    }

    @Test
    public void extractOfficeData_OFFICE_ADDRESS_INVALID__ERROR() {
        when(officeRepository.findAll(any(Specification.class))).thenReturn(List.of());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.extractOfficeData("Sofia, Cherkovna, 22");
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_ADDRESS_INVALID);
    }

    @Test
    public void extractOfficeData_OFFICE_ADDRESS_DATA_INVALID__ERROR() {
        when(officeRepository.findAll(any(Specification.class))).thenReturn(List.of());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    officeService.extractOfficeData("Sofia, Cherkovna");
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_ADDRESS_DATA_INVALID);
    }
}

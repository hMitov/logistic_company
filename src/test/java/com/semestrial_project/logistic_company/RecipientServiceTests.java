package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientData;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Recipient;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.AddressRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeRepository;
import com.semestrial_project.logistic_company.domain.repository.RecipientRepository;
import com.semestrial_project.logistic_company.domain.repository.SenderRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.RecipientService;
import com.semestrial_project.logistic_company.domain.services.implementations.AddressServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.RecipientServiceImpl;
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

public class RecipientServiceTests {
    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String FIRST_NAME = "Ivana";
    private final String LAST_NAME_1 = "Sedloeva";
    private final String LAST_NAME_2 = "Milanova";
    private final CUSTOMER_TYPE TYPE_OF_CUSTOMER = CUSTOMER_TYPE.PHYSICAL;
    private final SHIPMENT_POINT POINT_OF_SHIPMENT = SHIPMENT_POINT.ADDRESS;
    private final SHIPMENT_POINT POINT_OF_SHIPMENT_1 = SHIPMENT_POINT.OFFICE;
    private final String TELEPHONE_1 = "0812347654";
    private final String TELEPHONE_2 = "0882871622";
    private final String EMAIL = "ivana@gmail.com";
    private final String SPECIAL_INSTRUCTIONS = "call on arrive";
    private final String CITY = "Plovdiv";
    private final String STREET = "Tsar Boris 3";
    private final String STREET_NUM = "10";
    private final String POST_CODE = "1580";
    private Recipient recipient1;
    private Recipient recipient2;
    private Address address1;
    private Address address2;
    private RecipientRepository recipientRepository;
    private AddressRepository addressRepository;
    private SenderRepository senderRepository;
    private OfficeRepository officeRepository;
    private DomainAdapter domainAdapter;
    private DomainValidator validator;
    private AddressService addressService;
    private RecipientService recipientService;


    @BeforeEach
    public void setUp() {
        addressRepository = mock(AddressRepository.class);
        recipientRepository = mock(RecipientRepository.class);
        officeRepository = mock(OfficeRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);
        validator = new DomainValidator();

        addressService = new AddressServiceImpl(
                addressRepository,
                recipientRepository,
                senderRepository,
                officeRepository,
                domainAdapter
        );

        recipientService = new RecipientServiceImpl(
                recipientRepository,
                domainAdapter,
                addressService,
                validator
        );

        recipient1 = Recipient.builder()
                .id(ID_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME_1)
                .customerType(TYPE_OF_CUSTOMER)
                .telephone(TELEPHONE_1)
                .email(EMAIL)
                .specialInstructions(SPECIAL_INSTRUCTIONS).build();

        recipient2 = recipient1.toBuilder()
                .lastName(LAST_NAME_2)
                .telephone(TELEPHONE_1).build();

        address1 = Address.builder()
                .id(ID_1)
                .city(CITY)
                .street(STREET)
                .streetNum(STREET_NUM)
                .postCode(POST_CODE)
                .activeOffice(true).build();

        address2 = address1.toBuilder().activeOffice(false).build();
    }

    @Test
    public void processRecipientData_TELEPHONE_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.setTelephone(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.TELEPHONE_REQUIRED);
    }

    @Test
    public void processRecipientData_EMAIL_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.setEmail(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMAIL_REQUIRED);
    }

    @Test
    public void processRecipientData_FIRST_NAME_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.setFirstName(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.FIRST_NAME_REQUIRED);
    }

    @Test
    public void processRecipientData_LAST_NAME_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.setLastName(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.LAST_NAME_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.setAddress(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_CITY_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.getAddress().setCity(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_CITY_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_POST_CODE_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.getAddress().setPostCode(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_POST_CODE_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_STREET_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.getAddress().setStreet(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_STREET_NUMBER_REQUIRED__ERROR() {
        RecipientData data = getCreateRecipientData();
        data.getAddress().setStreetNum(null);

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_NUMBER_REQUIRED);
    }

    @Test
    public void processRecipientData_ADDRESS_IS_A_VALID_OFFICE__ERROR() {
        RecipientData data = getCreateRecipientData();

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        when(recipientRepository.save(any(Recipient.class))).thenReturn(recipient1);
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address1);
        address1 = address1.toBuilder().recipient(recipient1).activeRecipient(true).build();

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_IS_A_VALID_OFFICE_ERROR);
    }


    @Test
    public void processRecipientData_OK() throws DomainException {
        RecipientData data = getCreateRecipientData();

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        when(recipientRepository.save(any(Recipient.class))).thenReturn(recipient1);
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address2);
        when(addressRepository.save(any(Address.class))).thenReturn(address2);

        address1 = address1.toBuilder().recipient(recipient1).activeRecipient(true).build();

        RecipientRs response = recipientService.processRecipientData(data, POINT_OF_SHIPMENT.toString());

        assertEquals(response.getId(), recipient1.getId());
        assertEquals(response.getFirstName(), recipient1.getFirstName());
        assertEquals(response.getLastName(), recipient1.getLastName());
        assertEquals(response.getTelephone(), recipient1.getTelephone());
        assertEquals(response.getEmail(), recipient1.getEmail());
        assertEquals(response.getSpecialInstructions(), recipient1.getSpecialInstructions());
        assertEquals(response.getAddress().getId(), address1.getId());
        assertEquals(response.getAddress().getStreet(), address1.getStreet());
        assertEquals(response.getAddress().getCity(), address1.getCity());
        assertEquals(response.getAddress().getStreetNum(), address1.getStreetNum());
        assertEquals(response.getAddress().getPostCode(), address1.getPostCode());
    }

    private RecipientData getCreateRecipientData() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        RecipientData data = new RecipientData();
        data.setFirstName(FIRST_NAME);
        data.setLastName(LAST_NAME_1);
        data.setCustomerType(CUSTOMER_TYPE.PHYSICAL.toString());
        data.setTelephone(TELEPHONE_1);
        data.setEmail(EMAIL);
        data.setAddress(addressRequest);
        data.setSpecialInstructions(SPECIAL_INSTRUCTIONS);

        return data;
    }

    @Test
    public void updateRecipient_RECIPIENT_NOT_FOUND__ERROR() {
        RecipientData data = getUpdateRecipientData();

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.RECIPIENT_NOT_FOUND);
    }

    @Test
    public void updateRecipient_TELEPHONE_BLANK__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.setTelephone("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.TELEPHONE_BLANK);
    }

    @Test
    public void updateRecipient_EMAIL_BLANK__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.setEmail("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMAIL_BLANK);
    }

    @Test
    public void updateRecipient_FIRST_NAME_BLANK__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.setFirstName("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.FIRST_NAME_BLANK);
    }

    @Test
    public void updateRecipient_LAST_NAME_BLANK__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.setLastName("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.LAST_NAME_BLANK);
    }

    @Test
    public void updateRecipient_ADDRESS_CITY_REQUIRED__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.getAddress().setCity("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_CITY_REQUIRED);
    }

    @Test
    public void updateRecipient_ADDRESS_POST_CODE_REQUIRED__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.getAddress().setPostCode("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_POST_CODE_REQUIRED);
    }

    @Test
    public void updateRecipient_ADDRESS_STREET_REQUIRED__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.getAddress().setStreet("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_REQUIRED);
    }

    @Test
    public void updateRecipient_ADDRESS_STREET_NUMBER_REQUIRED__ERROR() {
        RecipientData data = getUpdateRecipientData();
        data.getAddress().setStreetNum("");

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_NUMBER_REQUIRED);
    }

    @Test
    public void updateRecipient_OK() throws DomainException {
        RecipientData data = getUpdateRecipientData();

        when(recipientRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(recipient1));
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address1));
        when(recipientRepository.save(any(Recipient.class))).thenReturn(recipient2);

        RecipientRs response = recipientService.updateRecipient(ID_1, data, POINT_OF_SHIPMENT_1.toString());

        assertEquals(response.getId(), recipient2.getId());
        assertEquals(response.getFirstName(), recipient2.getFirstName());
        assertEquals(response.getLastName(), recipient2.getLastName());
        assertEquals(response.getTelephone(), recipient2.getTelephone());
        assertEquals(response.getEmail(), recipient2.getEmail());
        assertEquals(response.getSpecialInstructions(), recipient2.getSpecialInstructions());
        assertEquals(response.getAddress().getId(), address1.getId());
        assertEquals(response.getAddress().getStreet(), address1.getStreet());
        assertEquals(response.getAddress().getCity(), address1.getCity());
        assertEquals(response.getAddress().getStreetNum(), address1.getStreetNum());
        assertEquals(response.getAddress().getPostCode(), address1.getPostCode());
    }

    private RecipientData getUpdateRecipientData() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        RecipientData data = new RecipientData();
        data.setFirstName(FIRST_NAME);
        data.setLastName(LAST_NAME_2);
        data.setCustomerType(CUSTOMER_TYPE.PHYSICAL.toString());
        data.setTelephone(TELEPHONE_1);
        data.setEmail(EMAIL);
        data.setAddress(addressRequest);
        data.setSpecialInstructions(SPECIAL_INSTRUCTIONS);

        return data;
    }

    @Test
    public void getRecipientById_OK() throws DomainException {
        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));

        RecipientResponse response = recipientService.getRecipientById(ID_1);

        assertEquals(response.getId(), recipient1.getId());
        assertEquals(response.getFirstName(), recipient1.getFirstName());
        assertEquals(response.getLastName(), recipient1.getLastName());
        assertEquals(response.getTelephone(), recipient1.getTelephone());
        assertEquals(response.getEmail(), recipient1.getEmail());
        assertEquals(response.getSpecialInstructions(), recipient1.getSpecialInstructions());
    }

    @Test
    public void getAllRecipients_OK() {
        when(recipientRepository.findAll()).thenReturn(List.of(recipient1, recipient2));

        List<RecipientResponse> response = recipientService.getAllRecipients();

        assertEquals(response.size(), 2);
    }
}
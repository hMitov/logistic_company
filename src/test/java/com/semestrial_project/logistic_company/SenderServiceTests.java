package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderData;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Sender;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode;
import com.semestrial_project.logistic_company.domain.repository.AddressRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeRepository;
import com.semestrial_project.logistic_company.domain.repository.RecipientRepository;
import com.semestrial_project.logistic_company.domain.repository.SenderRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.SenderService;
import com.semestrial_project.logistic_company.domain.services.implementations.AddressServiceImpl;
import com.semestrial_project.logistic_company.domain.services.implementations.SenderServiceImpl;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SenderServiceTests {

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
    private Sender sender1;
    private Sender sender2;
    private Address address1;
    private Address address2;
    private SenderRepository senderRepository;
    private AddressRepository addressRepository;
    private RecipientRepository recipientRepository;
    private OfficeRepository officeRepository;
    private DomainAdapter domainAdapter;
    private DomainValidator validator;
    private AddressService addressService;
    private SenderService senderService;


    @BeforeEach
    public void setUp() {
        senderRepository = mock(SenderRepository.class);
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

        senderService = new SenderServiceImpl(
                senderRepository,
                domainAdapter,
                addressService,
                validator
        );

        sender1 = Sender.builder()
                .id(ID_1)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME_1)
                .customerType(TYPE_OF_CUSTOMER)
                .telephone(TELEPHONE_1)
                .email(EMAIL)
                .specialInstructions(SPECIAL_INSTRUCTIONS).build();

        sender2 = sender1.toBuilder()
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
    public void processSenderData_TELEPHONE_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.setTelephone(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.TELEPHONE_REQUIRED);
    }

    @Test
    public void processSenderData_EMAIL_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.setEmail(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMAIL_REQUIRED);
    }

    @Test
    public void processSenderData_FIRST_NAME_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.setFirstName(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.FIRST_NAME_REQUIRED);
    }

    @Test
    public void processSenderData_LAST_NAME_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.setLastName(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.LAST_NAME_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.setAddress(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_CITY_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.getAddress().setCity(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_CITY_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_POST_CODE_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.getAddress().setPostCode(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_POST_CODE_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_STREET_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.getAddress().setStreet(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_STREET_NUMBER_REQUIRED__ERROR() {
        SenderData data = getCreateSenderData();
        data.getAddress().setStreetNum(null);

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_NUMBER_REQUIRED);
    }

    @Test
    public void processSenderData_ADDRESS_IS_A_VALID_OFFICE__ERROR() throws DomainException {
        SenderData data = getCreateSenderData();

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        when(senderRepository.save(any(Sender.class))).thenReturn(sender1);
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address1);
        address1 = address1.toBuilder().sender(sender1).activeSender(true).build();

        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_IS_A_VALID_OFFICE_ERROR);
    }


    @Test
    public void processSenderData_OK() throws DomainException {
        SenderData data = getCreateSenderData();

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.empty());
        when(senderRepository.save(any(Sender.class))).thenReturn(sender1);
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address2);
        when(addressRepository.save(any(Address.class))).thenReturn(address2);

        address1 = address1.toBuilder().sender(sender1).activeSender(true).build();

        SenderRs response = senderService.processSenderData(data, POINT_OF_SHIPMENT.toString());

        assertEquals(response.getId(), sender1.getId());
        assertEquals(response.getFirstName(), sender1.getFirstName());
        assertEquals(response.getLastName(), sender1.getLastName());
        assertEquals(response.getTelephone(), sender1.getTelephone());
        assertEquals(response.getEmail(), sender1.getEmail());
        assertEquals(response.getSpecialInstructions(), sender1.getSpecialInstructions());
        assertEquals(response.getAddress().getId(), address1.getId());
        assertEquals(response.getAddress().getStreet(), address1.getStreet());
        assertEquals(response.getAddress().getCity(), address1.getCity());
        assertEquals(response.getAddress().getStreetNum(), address1.getStreetNum());
        assertEquals(response.getAddress().getPostCode(), address1.getPostCode());
    }

    private SenderData getCreateSenderData() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        SenderData data = new SenderData();
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
    public void updateSender_SENDER_NOT_FOUND__ERROR() {
        SenderData data = getUpdateSenderData();

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.SENDER_NOT_FOUND);
    }

    @Test
    public void updateSender_TELEPHONE_BLANK__ERROR() {
        SenderData data = getUpdateSenderData();
        data.setTelephone("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.TELEPHONE_BLANK);
    }

    @Test
    public void updateSender_EMAIL_BLANK__ERROR() {
        SenderData data = getUpdateSenderData();
        data.setEmail("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.EMAIL_BLANK);
    }

    @Test
    public void updateSender_FIRST_NAME_BLANK__ERROR() {
        SenderData data = getUpdateSenderData();
        data.setFirstName("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.FIRST_NAME_BLANK);
    }

    @Test
    public void updateSender_LAST_NAME_BLANK__ERROR() {
        SenderData data = getUpdateSenderData();
        data.setLastName("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.LAST_NAME_BLANK);
    }

    @Test
    public void updateSender_ADDRESS_CITY_REQUIRED__ERROR() {
        SenderData data = getUpdateSenderData();
        data.getAddress().setCity("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_CITY_REQUIRED);
    }

    @Test
    public void updateSender_ADDRESS_POST_CODE_REQUIRED__ERROR() {
        SenderData data = getUpdateSenderData();
        data.getAddress().setPostCode("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_POST_CODE_REQUIRED);
    }

    @Test
    public void updateSender_ADDRESS_STREET_REQUIRED__ERROR() {
        SenderData data = getUpdateSenderData();
        data.getAddress().setStreet("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_REQUIRED);
    }

    @Test
    public void updateSender_ADDRESS_STREET_NUMBER_REQUIRED__ERROR() {
        SenderData data = getUpdateSenderData();
        data.getAddress().setStreetNum("");

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT.toString());
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.ADDRESS_STREET_NUMBER_REQUIRED);
    }

    @Test
    public void updateSender_OK() throws DomainException {
        SenderData data = getUpdateSenderData();

        when(senderRepository.findByTelephone(any(String.class))).thenReturn(Optional.of(sender1));
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address1));
        when(senderRepository.save(any(Sender.class))).thenReturn(sender2);

        SenderRs response = senderService.updateSender(ID_1, data, POINT_OF_SHIPMENT_1.toString());

        assertEquals(response.getId(), sender2.getId());
        assertEquals(response.getFirstName(), sender2.getFirstName());
        assertEquals(response.getLastName(), sender2.getLastName());
        assertEquals(response.getTelephone(), sender2.getTelephone());
        assertEquals(response.getEmail(), sender2.getEmail());
        assertEquals(response.getSpecialInstructions(), sender2.getSpecialInstructions());
        assertEquals(response.getAddress().getId(), address1.getId());
        assertEquals(response.getAddress().getStreet(), address1.getStreet());
        assertEquals(response.getAddress().getCity(), address1.getCity());
        assertEquals(response.getAddress().getStreetNum(), address1.getStreetNum());
        assertEquals(response.getAddress().getPostCode(), address1.getPostCode());
    }

    private SenderData getUpdateSenderData() {
        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setCity(CITY);
        addressRequest.setStreet(STREET);
        addressRequest.setStreetNum(STREET_NUM);
        addressRequest.setPostCode(POST_CODE);

        SenderData data = new SenderData();
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
    public void getSenderById_OK() throws DomainException {
        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));

        SenderResponse response = senderService.getSenderById(ID_1);

        assertEquals(response.getId(), sender1.getId());
        assertEquals(response.getFirstName(), sender1.getFirstName());
        assertEquals(response.getLastName(), sender1.getLastName());
        assertEquals(response.getTelephone(), sender1.getTelephone());
        assertEquals(response.getEmail(), sender1.getEmail());
        assertEquals(response.getSpecialInstructions(), sender1.getSpecialInstructions());
    }

    @Test
    public void getAllSenders_OK() {
        when(senderRepository.findAll()).thenReturn(List.of(sender1, sender2));

        List<SenderResponse> response = senderService.getAllSenders();

        assertEquals(response.size(), 2);
    }
}

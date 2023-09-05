package com.semestrial_project.logistic_company;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderData;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.Recipient;
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
import com.semestrial_project.logistic_company.domain.services.implementations.AddressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddressServiceTests {

    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final String CITY = "Plovdiv";
    private final String STREET = "Tsar Boris 3";
    private final String STREET_NUM = "10";
    private final String POST_CODE = "1580";
    private final String FIRST_NAME_1 = "Ivana";
    private final String FIRST_NAME_2 = "Zlatina";
    private final String LAST_NAME_1 = "Sedloeva";
    private final String LAST_NAME_2 = "Milanova";
    private final CUSTOMER_TYPE TYPE_OF_CUSTOMER = CUSTOMER_TYPE.PHYSICAL;
    private final SHIPMENT_POINT POINT_OF_SHIPMENT = SHIPMENT_POINT.ADDRESS;
    private final SHIPMENT_POINT POINT_OF_SHIPMENT_1 = SHIPMENT_POINT.OFFICE;
    private final String TELEPHONE_1 = "0812347654";
    private final String TELEPHONE_2 = "0882871622";
    private final String EMAIL = "ivana@gmail.com";
    private final String SPECIAL_INSTRUCTIONS = "call on arrive";
    private Address address1;
    private Address address2;
    private Recipient recipient1;
    private Recipient recipient2;
    private Sender sender1;
    private Office office1;
    private AddressRepository addressRepository;
    private RecipientRepository recipientRepository;
    private SenderRepository senderRepository;
    private OfficeRepository officeRepository;
    private DomainAdapter domainAdapter;
    private AddressService addressService;

    @BeforeEach
    public void setUp() {
        addressRepository = mock(AddressRepository.class);
        recipientRepository = mock(RecipientRepository.class);
        senderRepository = mock(SenderRepository.class);
        officeRepository = mock(OfficeRepository.class);
        domainAdapter = Mappers.getMapper(DomainAdapter.class);

        addressService = new AddressServiceImpl(
                addressRepository,
                recipientRepository,
                senderRepository,
                officeRepository,
                domainAdapter
        );

        recipient1 = Recipient.builder()
                .id(ID_1)
                .firstName(FIRST_NAME_1)
                .lastName(LAST_NAME_1)
                .customerType(TYPE_OF_CUSTOMER)
                .telephone(TELEPHONE_1)
                .email(EMAIL)
                .specialInstructions(SPECIAL_INSTRUCTIONS).build();

        recipient2 = Recipient.builder().firstName(FIRST_NAME_2).lastName(LAST_NAME_2).build();

        sender1 = Sender.builder()
                .id(ID_1)
                .firstName(FIRST_NAME_2)
                .lastName(LAST_NAME_2)
                .customerType(TYPE_OF_CUSTOMER)
                .telephone(TELEPHONE_2)
                .email(EMAIL)
                .specialInstructions(SPECIAL_INSTRUCTIONS).build();

        address1 = Address.builder()
                .id(ID_1)
                .city(CITY)
                .street(STREET)
                .streetNum(STREET_NUM)
                .postCode(POST_CODE)
                .recipient(recipient1)
                .build();

        address2 = address1.toBuilder()
                .recipient(null)
                .activeRecipient(false)
                .sender(sender1)
                .activeSender(true)
                .build();

        office1 = Office.builder()
                .id(ID_1)
                .dateOfEstablishment(LocalDate.now())
                .telephone(TELEPHONE_1)
                .build();
    }

    @Test
    public void registerAddressToRecipient_RECIPIENT_NOT_FOUND__ERROR() {
        AddressRequest data = getAddressRequest();

        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    addressService.registerAddressToRecipient(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.RECIPIENT_NOT_FOUND);
    }

    @Test
    public void registerAddressToRecipient_WITH_ACTIVE_RECIPIENT__OK() throws DomainException {
        AddressRequest data = getAddressRequest();
        Address address3 = address1.toBuilder().recipient(recipient1).activeRecipient(true).build();

        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address3));
        when(addressRepository.save(any(Address.class))).thenReturn(address1, address3);

        addressService.registerAddressToRecipient(ID_1, data);

        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(2)).save(addressArgumentCaptor.capture());
        assertNotNull(addressArgumentCaptor);
        Address address = addressArgumentCaptor.getAllValues().get(1);

        assertEquals(address.getCity(), data.getCity());
        assertEquals(address.getStreet(), data.getStreet());
        assertEquals(address.getStreetNum(), data.getStreetNum());
        assertEquals(address.getPostCode(), data.getPostCode());
        assertEquals(address.isActiveRecipient(), address3.isActiveRecipient());
        assertThat(address.getRecipient()).usingRecursiveComparison().isEqualTo(address3.getRecipient());
    }

    @Test
    public void registerAddressToRecipient_WITHOUT_ACTIVE_RECIPIENT__OK() throws DomainException {
        AddressRequest data = getAddressRequest();
        Address address3 = address1.toBuilder().recipient(recipient1).activeRecipient(true).build();

        when(recipientRepository.findById(any(Long.class))).thenReturn(Optional.of(recipient1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address1, address3);

        addressService.registerAddressToRecipient(ID_1, data);

        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(2)).save(addressArgumentCaptor.capture());
        assertNotNull(addressArgumentCaptor);
        Address address = addressArgumentCaptor.getAllValues().get(1);

        assertEquals(address.getCity(), data.getCity());
        assertEquals(address.getStreet(), data.getStreet());
        assertEquals(address.getStreetNum(), data.getStreetNum());
        assertEquals(address.getPostCode(), data.getPostCode());
        assertEquals(address.isActiveRecipient(), address3.isActiveRecipient());
        assertThat(address.getRecipient()).usingRecursiveComparison().isEqualTo(address3.getRecipient());
    }

    @Test
    public void registerAddressToSender_SENDER_NOT_FOUND__ERROR() {
        AddressRequest data = getAddressRequest();

        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    addressService.registerAddressToSender(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.SENDER_NOT_FOUND);
    }

    @Test
    public void registerAddressToSender_WITH_ACTIVE_SENDER__OK() throws DomainException {
        AddressRequest data = getAddressRequest();
        Address address3 = address1.toBuilder().sender(sender1).activeSender(true).build();

        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of(address3));
        when(addressRepository.save(any(Address.class))).thenReturn(address1, address3);

        addressService.registerAddressToSender(ID_1, data);

        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(2)).save(addressArgumentCaptor.capture());
        assertNotNull(addressArgumentCaptor);
        Address address = addressArgumentCaptor.getAllValues().get(1);

        assertEquals(address.getCity(), data.getCity());
        assertEquals(address.getStreet(), data.getStreet());
        assertEquals(address.getStreetNum(), data.getStreetNum());
        assertEquals(address.getPostCode(), data.getPostCode());
        assertEquals(address.isActiveSender(), address3.isActiveSender());
        assertThat(address.getSender()).usingRecursiveComparison().isEqualTo(address3.getSender());
    }

    @Test
    public void registerAddressToSender_WITHOUT_ACTIVE_SENDER__OK() throws DomainException {
        AddressRequest data = getAddressRequest();
        Address address3 = address1.toBuilder().sender(sender1).activeSender(true).build();

        when(senderRepository.findById(any(Long.class))).thenReturn(Optional.of(sender1));
        when(addressRepository.findAll(any(Specification.class))).thenReturn(List.of());
        when(addressRepository.save(any(Address.class))).thenReturn(address1, address3);

        addressService.registerAddressToSender(ID_1, data);

        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(2)).save(addressArgumentCaptor.capture());
        assertNotNull(addressArgumentCaptor);
        Address address = addressArgumentCaptor.getAllValues().get(1);

        assertEquals(address.getCity(), data.getCity());
        assertEquals(address.getStreet(), data.getStreet());
        assertEquals(address.getStreetNum(), data.getStreetNum());
        assertEquals(address.getPostCode(), data.getPostCode());
        assertEquals(address.isActiveSender(), address3.isActiveSender());
        assertThat(address.getSender()).usingRecursiveComparison().isEqualTo(address3.getSender());
    }

    @Test
    public void registerAddressToOffice_OFFICE_NOT_FOUND__ERROR() {
        AddressRequest data = getAddressRequest();

        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        DomainException assertEx = assertThrows(DomainException.class,
                () -> {
                    addressService.registerAddressToOffice(ID_1, data);
                });

        assertEquals(assertEx.getErrorCode(), DomainErrorCode.OFFICE_NOT_FOUND);
    }

    @Test
    public void registerAddressToOffice_OK() throws DomainException {
        AddressRequest data = getAddressRequest();

        when(officeRepository.findById(any(Long.class))).thenReturn(Optional.of(office1));
        when(addressRepository.save(any(Address.class))).thenReturn(address1);
        AddressResponse response = addressService.registerAddressToOffice(ID_1, data);

        assertEquals(response.getId(), ID_1);
        assertEquals(response.getPostCode(), data.getPostCode());
        assertEquals(response.getCity(), data.getCity());
        assertEquals(response.getStreet(), data.getStreet());
        assertEquals(response.getStreetNum(), data.getStreetNum());
    }

    private AddressRequest getAddressRequest() {
        AddressRequest data = new AddressRequest();
        data.setCity(CITY);
        data.setStreet(STREET);
        data.setStreetNum(STREET_NUM);
        data.setPostCode(POST_CODE);

        return data;
    }
}

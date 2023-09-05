package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.entity.Address;
import com.semestrial_project.logistic_company.domain.entity.Office;
import com.semestrial_project.logistic_company.domain.entity.Recipient;
import com.semestrial_project.logistic_company.domain.entity.Sender;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.AddressRepository;
import com.semestrial_project.logistic_company.domain.repository.OfficeRepository;
import com.semestrial_project.logistic_company.domain.repository.RecipientRepository;
import com.semestrial_project.logistic_company.domain.repository.SenderRepository;
import com.semestrial_project.logistic_company.domain.repository.specification.AddressSpecification;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;
import static com.semestrial_project.logistic_company.domain.repository.specification.AddressSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final RecipientRepository recipientRepository;

    private final SenderRepository senderRepository;

    private final OfficeRepository officeRepository;

    private final DomainAdapter domainAdapter;

    public AddressResponse registerAddressToRecipient(Long id, AddressRequest addressRequest) throws DomainException {
        Recipient recipient = getRecipient(id);
        List<Address> activeRecipientAddresses = addressRepository.findAll(where(withRecipientIdAndActiveRecipient(id)));
        if (!activeRecipientAddresses.isEmpty()) {
            Address foundAddress = activeRecipientAddresses.get(0).toBuilder()
                    .activeRecipient(false)
                    .build();
            addressRepository.save(foundAddress);
        }

        Address address = checkIfAddressExistsOrElseCreate(addressRequest);
        if(address.isActiveOffice()) {
            throw new DomainException(ADDRESS_IS_A_VALID_OFFICE_ERROR);
        }
        address = address.toBuilder()
                .recipient(recipient)
                .activeRecipient(true)
                .build();

        return domainAdapter.convertFromAddressToAddressResponse(addressRepository.save(address));
    }

    public AddressResponse registerAddressToSender(Long id, AddressRequest addressRequest) throws DomainException {
        Sender sender = getSender(id);
        List<Address> activeSenderAddresses = addressRepository.findAll(where(withSenderIdAndActiveSender(id)));
        if (!activeSenderAddresses.isEmpty()) {
            Address foundAddress = activeSenderAddresses.get(0).toBuilder()
                    .activeSender(false)
                    .build();
            addressRepository.save(foundAddress);
        }

        Address address = checkIfAddressExistsOrElseCreate(addressRequest);
        if(address.isActiveOffice()) {
            throw new DomainException(ADDRESS_IS_A_VALID_OFFICE_ERROR);
        }
        address = address.toBuilder()
                .sender(sender)
                .activeSender(true)
                .build();

        return domainAdapter.convertFromAddressToAddressResponse(addressRepository.save(address));
    }

    @Transactional(rollbackFor = DomainException.class)
    public AddressResponse registerAddressToOffice(Long id, AddressRequest addressRequest) throws DomainException {
        Office office = getOffice(id);
        Address address = checkIfAddressExistsOrElseCreate(addressRequest);
        address = address.toBuilder()
                .office(office)
                .activeOffice(true)
                .build();

        return domainAdapter.convertFromAddressToAddressResponse(addressRepository.save(address));
    }

    @Transactional(rollbackFor = DomainException.class)
    public AddressResponse updateOfficeAddress(Long id, AddressRequest addressRequest) throws DomainException {
        Office office = getOffice(id);
        List<Address> currentOfficeAddress = addressRepository.findAll(where(withOfficeIdAndActiveOffice(id)));
        if (!currentOfficeAddress.isEmpty()) {
            Address currentAddress = currentOfficeAddress.get(0);
            if (getAddress(addressRequest).isEmpty() || !Objects.deepEquals(currentAddress, getAddress(addressRequest).get(0))) {
                Address updatedAddress = currentAddress.toBuilder()
                        .activeOffice(false)
                        .build();
                addressRepository.save(updatedAddress);
            }
        }

        Address address = !getAddress(addressRequest).isEmpty() ? getAddress(addressRequest).get(0) : null;
        if(Objects.nonNull(address)) {
            address = address.toBuilder()
                    .city(Optional.of(addressRequest.getCity()).orElse(address.getCity()))
                    .street(Optional.of(addressRequest.getStreet()).orElse(address.getStreet()))
                    .streetNum(Optional.of(addressRequest.getStreetNum()).orElse(address.getStreetNum()))
                    .postCode(Optional.of(addressRequest.getPostCode()).orElse(address.getPostCode()))
                    .activeOffice(true)
                    .office(office)
                    .build();
        } else {
            address = Address.builder()
                    .city(addressRequest.getCity())
                    .street(addressRequest.getStreet())
                    .streetNum(addressRequest.getStreetNum())
                    .postCode(addressRequest.getPostCode())
                    .activeOffice(true)
                    .office(office)
                    .build();
        }

        return domainAdapter.convertFromAddressToAddressResponse(addressRepository.save(address));
    }

    public AddressResponse getRecipientActiveAddress(Long id) throws DomainException {
        List<Address> activeRecipientAddresses = addressRepository.findAll(where(withRecipientIdAndActiveRecipient(id)));
        if (activeRecipientAddresses.isEmpty()) {
            throw new DomainException(RECIPIENT_INACTIVE_ADDRESS);
        }

        return domainAdapter.convertFromAddressToAddressResponse(activeRecipientAddresses.get(0));
    }

    public AddressResponse getSenderActiveAddress(Long id) throws DomainException {
        List<Address> activeSenderAddresses = addressRepository.findAll(where(withSenderIdAndActiveSender(id)));

        return domainAdapter.convertFromAddressToAddressResponse(!activeSenderAddresses.isEmpty() ? activeSenderAddresses.get(0) : new Address());
    }

    public AddressResponse getOfficeActiveAddress(Long id) {
        List<Address> activeOfficeAddresses = addressRepository.findAll(where(withOfficeIdAndActiveOffice(id)));
        if (activeOfficeAddresses.isEmpty()) {
            return new AddressResponse();
        }
        return domainAdapter.convertFromAddressToAddressResponse(activeOfficeAddresses.get(0));
    }

    private Recipient getRecipient(Long id) throws DomainException {
        return recipientRepository.findById(id)
                .orElseThrow(() -> new DomainException(RECIPIENT_NOT_FOUND));
    }

    private Sender getSender(Long id) throws DomainException {
        return senderRepository.findById(id)
                .orElseThrow(() -> new DomainException(SENDER_NOT_FOUND));
    }

    private Office getOffice(Long id) throws DomainException {
        return officeRepository.findById(id)
                .orElseThrow(() -> new DomainException(OFFICE_NOT_FOUND));
    }

    public Address checkIfAddressExistsOrElseCreate(AddressRequest addressRequest) {
        List<Address> addressesLoaded = getAddress(addressRequest);
        return addressesLoaded.isEmpty() ?
                addressRepository.save(
                        Address.builder()
                                .city(addressRequest.getCity())
                                .postCode(addressRequest.getPostCode())
                                .street(addressRequest.getStreet())
                                .streetNum(addressRequest.getStreetNum())
                                .activeOffice(false)
                                .activeRecipient(false)
                                .activeSender(false)
                                .build()) : addressesLoaded.get(0);
    }

    private List<Address> getAddress(AddressRequest addressRequest) {
        List<Address> addressesLoaded = addressRepository.findAll(
                where(AddressSpecification.withCityStreetAndStreetNum(
                        addressRequest.getCity(),
                        addressRequest.getStreet(),
                        addressRequest.getStreetNum())));
        return addressesLoaded;
    }
}

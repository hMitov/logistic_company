package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderData;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderResponse;
import com.semestrial_project.logistic_company.domain.dto.sender.SenderRs;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.entity.Sender;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.SenderRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.SenderService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.SENDER_NOT_FOUND;

@Service
@AllArgsConstructor
public class SenderServiceImpl implements SenderService {

    private final SenderRepository senderRepository;

    private final DomainAdapter domainAdapter;

    private final AddressService addressService;

    private final DomainValidator validator;

    @Transactional(rollbackFor = DomainException.class)
    public SenderRs processSenderData(SenderData senderData, String departurePoint) throws DomainException {
        Optional<Sender> senderLoaded = senderRepository.findByTelephone(senderData.getTelephone());
        if (senderLoaded.isPresent()) {
            return updateSender(senderLoaded.get().getId(), senderData, departurePoint);
        }
        validator.validateCustomerOnCreate(senderData, departurePoint);

        CUSTOMER_TYPE customerType = CUSTOMER_TYPE.valueOf(senderData.getCustomerType().toUpperCase());

        Sender sender = Sender.builder()
                .firstName(senderData.getFirstName())
                .lastName(senderData.getLastName())
                .organizationName(senderData.getOrganizationName())
                .customerType(customerType)
                .telephone(senderData.getTelephone())
                .email(senderData.getEmail())
                .specialInstructions(senderData.getSpecialInstructions())
                .build();
        Sender savedSender = this.senderRepository.save(sender);

        AddressResponse address;
        if (departurePoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            address = addressService.registerAddressToSender(savedSender.getId(), senderData.getAddress());
        } else {
            address = new AddressResponse();
        }
        return domainAdapter.convertToSenderRs(savedSender, address);
    }


    public SenderRs updateSender(Long id, SenderData senderData, String departurePoint) throws DomainException {
        Sender senderLoaded = getSender(id);
        validator.validateCustomerOnUpdate(senderData, senderLoaded, departurePoint);

        AddressResponse address;
        if (departurePoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            address = addressService.registerAddressToSender(id, senderData.getAddress());
        } else {
            address = addressService.getSenderActiveAddress(id);
        }

        if (senderData.getCustomerType().equalsIgnoreCase(CUSTOMER_TYPE.PHYSICAL.toString())) {
            senderLoaded = senderLoaded.toBuilder()
                    .firstName(Optional.of(senderData.getFirstName()).orElse(senderLoaded.getFirstName()))
                    .lastName(Optional.of(senderData.getLastName()).orElse(senderLoaded.getLastName()))
                    .organizationName(null)
                    .customerType(CUSTOMER_TYPE.PHYSICAL)
                    .telephone(Optional.ofNullable(senderData.getTelephone()).orElse(senderLoaded.getTelephone()))
                    .email(Optional.ofNullable(senderData.getEmail()).orElse(senderLoaded.getEmail()))
                    .build();
        } else {
            senderLoaded = senderLoaded.toBuilder()
                    .firstName(null)
                    .lastName(null)
                    .organizationName(Optional.ofNullable(senderData.getOrganizationName()).orElse(senderLoaded.getOrganizationName()))
                    .customerType(CUSTOMER_TYPE.ORGANIZATION)
                    .telephone(Optional.ofNullable(senderData.getTelephone()).orElse(senderLoaded.getTelephone()))
                    .email(Optional.ofNullable(senderData.getEmail()).orElse(senderLoaded.getEmail()))
                    .build();
        }

        Sender savedSender = this.senderRepository.save(senderLoaded);

        return domainAdapter.convertToSenderRs(savedSender, address);
    }

    private Sender getSender(Long id) throws DomainException {
        return senderRepository.findById(id)
                .orElseThrow(() -> new DomainException(SENDER_NOT_FOUND));
    }

    public SenderResponse getSenderById(Long id) throws DomainException {
        return domainAdapter.convertToSenderResponse(senderRepository.findById(id)
                .orElseThrow(() -> new DomainException(SENDER_NOT_FOUND)));
    }

    public List<SenderResponse> getAllSenders() {
        return senderRepository.findAll().stream()
                .map(domainAdapter::convertToSenderResponse).collect(Collectors.toList());
    }
}

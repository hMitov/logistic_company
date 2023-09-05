package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.adapter.DomainAdapter;
import com.semestrial_project.logistic_company.domain.dto.address.AddressResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientData;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientResponse;
import com.semestrial_project.logistic_company.domain.dto.recipient.RecipientRs;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import com.semestrial_project.logistic_company.domain.entity.Recipient;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.RecipientRepository;
import com.semestrial_project.logistic_company.domain.services.AddressService;
import com.semestrial_project.logistic_company.domain.services.RecipientService;
import com.semestrial_project.logistic_company.domain.services.validator.DomainValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.RECIPIENT_NOT_FOUND;

@Service
@AllArgsConstructor
public class RecipientServiceImpl implements RecipientService {

    private final RecipientRepository recipientRepository;

    private final DomainAdapter domainAdapter;

    private final AddressService addressService;

    private final DomainValidator validator;

    public RecipientRs processRecipientData(RecipientData recipientData, String arrivalPoint) throws DomainException {
        Optional<Recipient> recipientLoaded = recipientRepository.findByTelephone(recipientData.getTelephone());
        if (recipientLoaded.isPresent()) {
            return updateRecipient(recipientLoaded.get().getId(), recipientData, arrivalPoint);
        }
        validator.validateCustomerOnCreate(recipientData, arrivalPoint);


        CUSTOMER_TYPE customerType = CUSTOMER_TYPE.valueOf(recipientData.getCustomerType().toUpperCase());

        Recipient recipient = Recipient.builder()
                .firstName(recipientData.getFirstName())
                .lastName(recipientData.getLastName())
                .organizationName(recipientData.getOrganizationName())
                .customerType(customerType)
                .telephone(recipientData.getTelephone())
                .email(recipientData.getEmail())
                .specialInstructions(recipientData.getSpecialInstructions())
                .build();
        Recipient savedRecipient = this.recipientRepository.save(recipient);

        AddressResponse address;
        if (arrivalPoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            address = addressService.registerAddressToRecipient(savedRecipient.getId(), recipientData.getAddress());
        } else {
            address = new AddressResponse();
        }

        return domainAdapter.convertToRecipientRs(savedRecipient, address);
    }

    public RecipientRs updateRecipient(Long id, RecipientData recipientData, String arrivalPoint) throws DomainException {
        Recipient recipientLoaded = getRecipient(id);
        validator.validateCustomerOnUpdate(recipientData, recipientLoaded, arrivalPoint);

        AddressResponse address;
        if (arrivalPoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            address  = addressService.registerAddressToRecipient(id, recipientData.getAddress());
        } else {
            address = addressService.getRecipientActiveAddress(id);
        }

        if (recipientData.getCustomerType().equalsIgnoreCase(CUSTOMER_TYPE.PHYSICAL.toString())) {
            recipientLoaded = recipientLoaded.toBuilder()
                    .firstName(Optional.of(recipientData.getFirstName()).orElse(recipientLoaded.getFirstName()))
                    .lastName(Optional.of(recipientData.getLastName()).orElse(recipientLoaded.getLastName()))
                    .organizationName(null)
                    .customerType(CUSTOMER_TYPE.PHYSICAL)
                    .telephone(Optional.ofNullable(recipientData.getTelephone()).orElse(recipientLoaded.getTelephone()))
                    .email(Optional.ofNullable(recipientData.getEmail()).orElse(recipientLoaded.getEmail()))
                    .specialInstructions(Optional.ofNullable(recipientData.getSpecialInstructions()).orElse(recipientLoaded.getSpecialInstructions()))
                    .build();
        } else {
            recipientLoaded = recipientLoaded.toBuilder()
                    .firstName(null)
                    .lastName(null)
                    .organizationName(Optional.ofNullable(recipientData.getOrganizationName()).orElse(recipientLoaded.getOrganizationName()))
                    .customerType(CUSTOMER_TYPE.ORGANIZATION)
                    .telephone(Optional.ofNullable(recipientData.getTelephone()).orElse(recipientLoaded.getTelephone()))
                    .email(Optional.ofNullable(recipientData.getEmail()).orElse(recipientLoaded.getEmail()))
                    .specialInstructions(Optional.ofNullable(recipientData.getSpecialInstructions()).orElse(recipientLoaded.getSpecialInstructions()))
                    .build();
        }

        Recipient savedRecipient = this.recipientRepository.save(recipientLoaded);

        return domainAdapter.convertToRecipientRs(savedRecipient, address);
    }

    private Recipient getRecipient(Long id) throws DomainException {
        return recipientRepository.findById(id)
                .orElseThrow(() -> new DomainException(RECIPIENT_NOT_FOUND));
    }

    public RecipientResponse getRecipientById(Long id) throws DomainException {
        return domainAdapter.convertToRecipientResponse(recipientRepository.findById(id)
                .orElseThrow(() -> new DomainException(RECIPIENT_NOT_FOUND)));
    }

    public List<RecipientResponse> getAllRecipients() {
        return recipientRepository.findAll().stream()
                .map(domainAdapter::convertToRecipientResponse).collect(Collectors.toList());
    }
}




package com.semestrial_project.logistic_company.domain.services.validator;

import com.semestrial_project.logistic_company.domain.dto.account.CreateAccount;
import com.semestrial_project.logistic_company.domain.dto.address.AddressRequest;
import com.semestrial_project.logistic_company.domain.dto.office.CreateOffice;
import com.semestrial_project.logistic_company.domain.dto.office.UpdateOffice;
import com.semestrial_project.logistic_company.domain.dto.office_employee.BaseEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.recipient.BaseCustomerData;
import com.semestrial_project.logistic_company.domain.dto.shipment.CompanyIncome;
import com.semestrial_project.logistic_company.domain.dto.shipment.CreateShipment;
import com.semestrial_project.logistic_company.domain.dto.shipment.UpdateShipment;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierData;
import com.semestrial_project.logistic_company.domain.dto.user.CreateUser;
import com.semestrial_project.logistic_company.domain.dto.vehicle.CreateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.UpdateVehicle;
import com.semestrial_project.logistic_company.domain.entity.*;
import com.semestrial_project.logistic_company.domain.entity.enums.CUSTOMER_TYPE;
import com.semestrial_project.logistic_company.domain.entity.enums.SHIPMENT_POINT;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;

@Component
public class DomainValidator {
    public <D extends BaseCustomerData> void validateCustomerOnCreate(D data, String addressPoint) throws DomainException {
        if (ObjectUtils.isEmpty(data.getTelephone())) {
            throw new DomainException(TELEPHONE_REQUIRED);
        } else {
            validateTelephonePattern(data.getTelephone());
        }
        if (ObjectUtils.isEmpty(data.getEmail())) {
            throw new DomainException(EMAIL_REQUIRED);
        }

        if (data.getCustomerType().equalsIgnoreCase(CUSTOMER_TYPE.PHYSICAL.toString())) {
            if (ObjectUtils.isEmpty(data.getFirstName())) {
                throw new DomainException(FIRST_NAME_REQUIRED);
            }
            if (ObjectUtils.isEmpty(data.getLastName())) {
                throw new DomainException(LAST_NAME_REQUIRED);
            }
        } else {
            if (ObjectUtils.isEmpty(data.getOrganizationName())) {
                throw new DomainException(ORGANIZATION_NAME_REQUIRED);
            }
        }
        if (addressPoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            if (ObjectUtils.isEmpty(data.getAddress())) {
                throw new DomainException(ADDRESS_REQUIRED);
            } else {
                validateAddress(data.getAddress());
            }
        }
    }

    private void validateTelephonePattern(String telephone) throws DomainException {
        String patterns
                = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?){2}\\d{3}$"
                + "|^(\\+\\d{1,3}( )?)?(\\d{3}[ ]?)(\\d{2}[ ]?){2}\\d{2}$";

        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher(telephone);
        if (!matcher.matches()) {
            throw new DomainException(TELEPHONE_INVALID_FORMAT);
        }
    }

    public <D extends BaseCustomerData, C extends BaseCustomer> void validateCustomerOnUpdate(D data, C dbCustomer, String addressPoint) throws DomainException {
        if (Objects.nonNull(data.getTelephone()) && !Objects.equals(dbCustomer.getTelephone(), data.getTelephone())) {
            if (!StringUtils.hasText(data.getTelephone())) {
                throw new DomainException(TELEPHONE_BLANK);
            } else {
                validateTelephonePattern(data.getTelephone());
            }
        }
        if (Objects.nonNull(data.getEmail()) && !Objects.equals(dbCustomer.getEmail(), data.getEmail())) {
            if (!StringUtils.hasText(data.getEmail())) {
                throw new DomainException(EMAIL_BLANK);
            }
        }

        if (data.getCustomerType().equalsIgnoreCase(CUSTOMER_TYPE.PHYSICAL.toString())) {
            if (Objects.nonNull(data.getFirstName()) && !Objects.equals(dbCustomer.getFirstName(), data.getFirstName())) {
                if (!StringUtils.hasText(data.getFirstName())) {
                    throw new DomainException(FIRST_NAME_BLANK);
                }
            }
            if (Objects.nonNull(data.getLastName()) && !Objects.equals(dbCustomer.getLastName(), data.getLastName())) {
                if (!StringUtils.hasText(data.getLastName())) {
                    throw new DomainException(LAST_NAME_BLANK);
                }
            }
        } else {
            if (Objects.nonNull(data.getOrganizationName()) && !Objects.equals(dbCustomer.getOrganizationName(), data.getOrganizationName())) {
                if (!StringUtils.hasText(data.getOrganizationName())) {
                    throw new DomainException(ORGANIZATION_NAME_BLANK);
                }
            }
        }

        if (addressPoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            if (Objects.nonNull(data.getAddress())) {
                validateAddress(data.getAddress());
            }
        }

        if (Objects.nonNull(data.getSpecialInstructions()) && !Objects.equals(dbCustomer.getSpecialInstructions(), data.getSpecialInstructions())) {
            if (!StringUtils.hasText(data.getSpecialInstructions())) {
                throw new DomainException(SPECIAL_INSTRUCTIONS_BLANK);
            }
        }
    }


    public void validateAddress(AddressRequest data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getCity())) {
            throw new DomainException(ADDRESS_CITY_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getPostCode())) {
            throw new DomainException(ADDRESS_POST_CODE_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getStreet())) {
            throw new DomainException(ADDRESS_STREET_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getStreetNum())) {
            throw new DomainException(ADDRESS_STREET_NUMBER_REQUIRED);
        }
    }

    public void validateShipmentOnCreate(CreateShipment data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getDeparturePoint())) {
            throw new DomainException(SHIPMENT_DEPARTURE_POINT_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getArrivalPoint())) {
            throw new DomainException(SHIPMENT_ARRIVAL_POINT_REQUIRED);
        }

        validateDeparturePoint(data.getDeparturePoint());
        validateArrivalPoint(data.getArrivalPoint());

        if (data.getDeparturePoint().equalsIgnoreCase(SHIPMENT_POINT.OFFICE.toString())) {
            if (ObjectUtils.isEmpty(data.getDepartureAddress())) {
                throw new DomainException(DEPARTURE_ADDRESS_REQUIRED);
            }
            if (ObjectUtils.isEmpty(data.getRegistrantEmployeeId())) {
                throw new DomainException(SHIPMENT_REGISTRANT_REQUIRED);
            }
        }
        if (data.getArrivalPoint().equalsIgnoreCase(SHIPMENT_POINT.OFFICE.toString())) {
            if (ObjectUtils.isEmpty(data.getArrivalAddress())) {
                throw new DomainException(ARRIVAL_ADDRESS_REQUIRED);
            }
        }
        if (ObjectUtils.isEmpty(data.getSender())) {
            throw new DomainException(SHIPMENT_SENDER_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getRecipient())) {
            throw new DomainException(SHIPMENT_RECIPIENT_REQUIRED);
        }
    }

    private static void validateArrivalPoint(String arrivalPoint) throws DomainException {
        if (!arrivalPoint.equalsIgnoreCase(SHIPMENT_POINT.OFFICE.toString()) && !arrivalPoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            throw new DomainException(ARRIVAL_POINT_INVALID);
        }
    }

    private static void validateDeparturePoint(String departurePoint) throws DomainException {
        if (!departurePoint.equalsIgnoreCase(SHIPMENT_POINT.OFFICE.toString()) && !departurePoint.equalsIgnoreCase(SHIPMENT_POINT.ADDRESS.toString())) {
            throw new DomainException(DEPARTURE_POINT_INVALID);
        }
    }

    public void validateShipmentOnUpdate(UpdateShipment data, Shipment dbShipment) throws DomainException {
        if (Objects.nonNull(data.getDeparturePoint()) && !dbShipment.getDeparturePoint().toString().equalsIgnoreCase(data.getDeparturePoint())) {
            validateDeparturePoint(data.getDeparturePoint());
        }
        if (Objects.nonNull(data.getArrivalPoint()) && !dbShipment.getArrivalPoint().toString().equalsIgnoreCase(data.getArrivalPoint())) {
            validateDeparturePoint(data.getArrivalPoint());
        }
        if (Objects.nonNull(data.getDepartureAddress())) {
            if (!StringUtils.hasText(data.getDepartureAddress())) {
                throw new DomainException(SHIPMENT_DEPARTURE_ADDRESS_BLANK);
            }
        }
        if (Objects.nonNull(data.getArrivalAddress())) {
            if (!StringUtils.hasText(data.getArrivalAddress())) {
                throw new DomainException(SHIPMENT_ARRIVAL_ADDRESS_BLANK);
            }
        }
        if (Objects.nonNull(data.getShipmentState()) && !Objects.equals(dbShipment.getShipmentState().getStateName(), data.getShipmentState())) {
            if (!StringUtils.hasText(data.getShipmentState())) {
                throw new DomainException(SHIPMENT_SATE_BLANK);
            }
        }
        validateCustomerOnUpdate(data.getSender(), dbShipment.getSender(), data.getDeparturePoint());
        validateCustomerOnUpdate(data.getRecipient(), dbShipment.getRecipient(), data.getArrivalPoint());
    }

    public <D extends BaseEmployeeData> void validateEmployeeOnCreate(D data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getEgn())) {
            throw new DomainException(EMPLOYEE_EGN_REQUIRED);
        } else {
            if (data.getEgn().length() != 10) {
                throw new DomainException(EMPLOYEE_EGN_INVALID);
            }
        }
        if (ObjectUtils.isEmpty(data.getFirstName())) {
            throw new DomainException(EMPLOYEE_FIRST_NAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getMiddleName())) {
            throw new DomainException(EMPLOYEE_MIDDLE_NAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getLastName())) {
            throw new DomainException(EMPLOYEE_LAST_NAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getDateOfEmploy())) {
            throw new DomainException(EMPLOYEE_EMPLOY_DATE_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getSalary())) {
            throw new DomainException(EMPLOYEE_SALARY_REQUIRED);
        } else {
            if (data.getSalary() <= 0.0) {
                throw new DomainException(EMPLOYEE_SALARY_INVALID);
            }
        }
        if (data instanceof OfficeEmployeeData) {
            OfficeEmployeeData officeEmployeeData = (OfficeEmployeeData) data;
            if (ObjectUtils.isEmpty(officeEmployeeData.getOfficeAddress())) {
                throw new DomainException(OFFICE_EMPLOYEE_OFFICE_ADDRESS_REQUIRED);
            }
        } else if (data instanceof SupplierData) {
            SupplierData supplierData = (SupplierData) data;
            if (ObjectUtils.isEmpty(supplierData.getVehicleId())) {
                throw new DomainException(SUPPLIER_VEHICLE_ID_REQUIRED);
            }
        }
    }

    public <D extends BaseEmployeeData, E extends BaseEmployee> void validateEmployeeOnUpdate(D data, E dbEmployee) throws DomainException {
        if (Objects.nonNull(data.getEgn()) && !Objects.equals(dbEmployee.getEgn(), data.getEgn())) {
            if (!StringUtils.hasText(data.getEgn())) {
                throw new DomainException(EMPLOYEE_EGN_BLANK);
            }
            if (data.getEgn().length() != 10) {
                throw new DomainException(EMPLOYEE_EGN_INVALID);
            }
        }
        if (Objects.nonNull(data.getFirstName()) && !Objects.equals(dbEmployee.getFirstName(), data.getFirstName())) {
            if (!StringUtils.hasText(data.getFirstName())) {
                throw new DomainException(EMPLOYEE_FIRST_NAME_BLANK);
            }
        }
        if (Objects.nonNull(data.getMiddleName()) && !Objects.equals(dbEmployee.getMiddleName(), data.getMiddleName())) {
            if (!StringUtils.hasText(data.getMiddleName())) {
                throw new DomainException(EMPLOYEE_MIDDLE_NAME_BLANK);
            }
        }
        if (Objects.nonNull(data.getLastName()) && !Objects.equals(dbEmployee.getLastName(), data.getLastName())) {
            if (!StringUtils.hasText(data.getLastName())) {
                throw new DomainException(EMPLOYEE_LAST_NAME_BLANK);
            }
        }
        if (Objects.nonNull(data.getSalary()) && !Objects.equals(dbEmployee.getSalary(), data.getSalary())) {
            if (data.getSalary() <= 0.0) {
                throw new DomainException(EMPLOYEE_SALARY_INVALID);
            }
        }

        if (data instanceof OfficeEmployeeData) {
            OfficeEmployeeData officeEmployeeData = (OfficeEmployeeData) data;
            if (Objects.nonNull(officeEmployeeData.getOfficeAddress())) {
                if (!StringUtils.hasText(officeEmployeeData.getOfficeAddress())) {
                    throw new DomainException(OFFICE_EMPLOYEE_ADDRESS_BLANK);
                }
            }
        }

        if (data instanceof SupplierData) {
            SupplierData supplierData = (SupplierData) data;
            if (ObjectUtils.isEmpty(supplierData.getVehicleId())) {
                throw new DomainException(SUPPLIER_VEHICLE_ID_REQUIRED);
            }

        }
    }

    public void validateVehicleOnCreate(CreateVehicle data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getBrand())) {
            throw new DomainException(VEHICLE_BRAND_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getModel())) {
            throw new DomainException(VEHICLE_MODEL_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getRegPlateNumber())) {
            throw new DomainException(VEHICLE_REGISTRATION_PLATE_REQUIRED);
        }
    }

    public void validateVehicleOnUpdate(UpdateVehicle data, Vehicle dbVehicle) throws DomainException {
        if (Objects.nonNull(data.getBrand()) && !Objects.equals(dbVehicle.getBrand(), data.getBrand())) {
            if (!StringUtils.hasText(data.getBrand())) {
                throw new DomainException(VEHICLE_BRAND_BLANK);
            }
        }
        if (Objects.nonNull(data.getModel()) && !Objects.equals(dbVehicle.getModel(), data.getModel())) {
            if (!StringUtils.hasText(data.getModel())) {
                throw new DomainException(VEHICLE_MODEL_BLANK);
            }
        }
        if (Objects.nonNull(data.getRegPlateNumber()) && !Objects.equals(dbVehicle.getRegPlateNumber(), data.getRegPlateNumber())) {
            if (!StringUtils.hasText(data.getRegPlateNumber())) {
                throw new DomainException(VEHICLE_REGISTRATION_PLATE_BLANK);
            }
        }
    }

    public void validateOfficeOnCreate(CreateOffice data) throws DomainException {
        validateAddress(data.getOfficeAddress());
        if (ObjectUtils.isEmpty(data.getTelephone())) {
            throw new DomainException(OFFICE_TELEPHONE_REQUIRED);
        } else {
            validateTelephonePattern(data.getTelephone());
        }
        if (ObjectUtils.isEmpty(data.getDateOfEstablishment())) {
            throw new DomainException(OFFICE_DATE_OF_ESTABLISHMENT_REQUIRED);
        }
    }

    public void validateOfficeOnUpdate(UpdateOffice data, Office dbOffice) throws DomainException {
        if (Objects.nonNull(data.getOfficeAddress())) {
            validateAddress(data.getOfficeAddress());
        }
        if (Objects.nonNull(data.getTelephone()) && !Objects.equals(dbOffice.getTelephone(), data.getTelephone())) {
            if (!StringUtils.hasText(data.getTelephone())) {
                throw new DomainException(OFFICE_TELEPHONE_BLANK);
            } else {
                validateTelephonePattern(data.getTelephone());
            }
        }
        if (Objects.nonNull(data.getDateOfEstablishment()) && !Objects.equals(dbOffice.getDateOfEstablishment().toString(), data.getDateOfEstablishment())) {
            if (!StringUtils.hasText(data.getDateOfEstablishment())) {
                throw new DomainException(OFFICE_DATE_OF_ESTABLISHMENT_BLANK);
            }
        }
    }

    public void validateIncomeFromShipments(CompanyIncome data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getStartDate())) {
            throw new DomainException(COMPANY_INCOME_PERIOD_START_DATE_REQ);
        }
        if (ObjectUtils.isEmpty(data.getEndDate())) {
            throw new DomainException(COMPANY_INCOME_PERIOD_END_DATE_REQ);
        }
    }

    public void validateAccountOnCreate(CreateAccount data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getFirstName())) {
            throw new DomainException(FIRST_NAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getLastName())) {
            throw new DomainException(LAST_NAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getTelephone())) {
            throw new DomainException(COMPANY_INCOME_PERIOD_START_DATE_REQ);
        }
        validateAddress(data.getAddress());
        validateUserOnCreate(data.getUser());
    }

    private void validateUserOnCreate(CreateUser data) throws DomainException {
        if (ObjectUtils.isEmpty(data.getUsername())) {
            throw new DomainException(USERNAME_REQUIRED);
        }
        if (ObjectUtils.isEmpty(data.getPassword())) {
            throw new DomainException(PASSWORD_REQUIRED);
        }
    }
}


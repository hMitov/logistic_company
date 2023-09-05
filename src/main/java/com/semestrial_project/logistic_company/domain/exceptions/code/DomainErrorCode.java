package com.semestrial_project.logistic_company.domain.exceptions.code;

public enum DomainErrorCode implements ErrorCode {

    RECIPIENT_INACTIVE_ADDRESS(1001, "Recipient with the specified id does not have active address."),
    SENDER_INACTIVE_ADDRESS(1002, "Sender with the specified id does not have active address."),
    RECIPIENT_NOT_FOUND(1003, "Not such recipient with the specified id was found."),
    SENDER_NOT_FOUND(1004, "Not such sender with the specified id was found."),
    OFFICE_NOT_FOUND(1005, "Not such office with the specified id was found."),
    EMPLOYEE_ALREADY_EXISTS(1006, "Employee with the specified egn already exists."),
    OFFICE_EMPLOYEE_NOT_FOUND(1007, "Not such office employee with the specified id was found."),
    OFFICE_ADDRESS_INVALID(1008, "Not such office address was found."),
    OFFICE_ADDRESS_DATA_INVALID(1009, "Office address data passed contains more information than needed."),
    SHIPMENT_STATE_ERROR(1010, "Shipment can be updated only if it is in Created state"),
    ADDRESS_NOT_FOUND(1011, "Not such address with the specified id was found."),
    REGISTRANT_NOT_FOUND(1012, "Not such an office employee with the specified employee id was found."),
    SHIPMENT_NOT_FOUND(1013, "Not such shipment with the specified id was found."),
    SHIPMENT_ALREADY_PROCESSED(1014, "Shipment has already being processed."),
    SHIPMENT_STATE_INVALID(1015, "No shipment state with the specified id was found."),
    VEHICLE_NOT_FOUND(1016, "No vehicle with the specified id was found."),
    VEHICLE_ALREADY_EXISTS(1017, "Vehicle with the same registration plate number already exists."),
    SUPPLIER_NOT_FOUND(1018, "No such supplier with the specified id was found."),
    DRIVING_CATEGORY_NOT_FOUND(1019, "No such Driving category found."),
    VEHICLE_SUPPLIERS_REACHED_MAXIMUM(1020, "Vehicle already has a maximum number of suppliers assigned. Please choose another vehicle."),
    SUPPLIER_EGN_NOT_FOUND(1018, "No such supplier with the specified egn was found."),
    TELEPHONE_REQUIRED(1019, "Telephone field is required."),
    EMAIL_REQUIRED(1020, "Email field is required."),
    FIRST_NAME_REQUIRED(1021, "First name field is required."),
    LAST_NAME_REQUIRED(1022, "Last name fild is required."),
    ORGANIZATION_NAME_REQUIRED(1023, "Organization name field is required when not physical."),
    ADDRESS_REQUIRED(1024, "Address field is required"),
    TELEPHONE_BLANK(1025, "In case of customer update, telephone field should not be blank."),
    EMAIL_BLANK(1026, "In case of customer update, email field should not be blank."),
    FIRST_NAME_BLANK(1027, "In case of customer update, first name field should not be blank."),
    LAST_NAME_BLANK(1028, "In case of customer update, last name field should not be blank."),
    ORGANIZATION_NAME_BLANK(1029, "In case of customer update, organization name field should not be blank."),
    SPECIAL_INSTRUCTIONS_BLANK(1030, "In case of customer update, special instructions field should not be blank."),
    ADDRESS_CITY_REQUIRED(1031, "Address city field is required."),
    ADDRESS_STREET_REQUIRED(1032, "Address street field is required."),
    ADDRESS_STREET_NUMBER_REQUIRED(1033, "Address street number field is required."),
    ADDRESS_POST_CODE_REQUIRED(1034, "Address post code field is required."),
    SHIPMENT_DEPARTURE_POINT_REQUIRED(1035, "Shipment departure point field is required."),
    SHIPMENT_ARRIVAL_POINT_REQUIRED(1036, "Shipment arrival point field is required."),
    DEPARTURE_ADDRESS_REQUIRED(1037, "In case departure point is office, departure address field is required."),
    ARRIVAL_ADDRESS_REQUIRED(1038, "In case arrival point is office, arrival address field is required."),
    SHIPMENT_SENDER_REQUIRED(1039, "Shipment sender data field is required."),
    SHIPMENT_RECIPIENT_REQUIRED(1040, "Shipment recipient data field is required."),
    SHIPMENT_REGISTRANT_REQUIRED(1041, "Shipment registrant employee id field is required."),
    ARRIVAL_POINT_INVALID(1042, "Arrival point is not a valid shipment start point."),
    DEPARTURE_POINT_INVALID(1043, "Departure point is not a valid shipment start point."),
    SHIPMENT_DEPARTURE_ADDRESS_BLANK(1044, "In case of shipment update, departure address field should not be blank."),
    SHIPMENT_ARRIVAL_ADDRESS_BLANK(1045, "In case of shipment update, arrival address field should not be blank."),
    SHIPMENT_SATE_BLANK(1046, "In case of shipment update, shipment state field should not be blank."),
    EMPLOYEE_EGN_REQUIRED(1047, "Employee egn field is required."),
    EMPLOYEE_FIRST_NAME_REQUIRED(1048, "Employee first name field is required."),
    EMPLOYEE_MIDDLE_NAME_REQUIRED(1049, "Employee middle name field is required."),
    EMPLOYEE_LAST_NAME_REQUIRED(1050, "Employee last name field is required."),
    EMPLOYEE_SALARY_REQUIRED(1051, "Employee salary field is required."),
    OFFICE_EMPLOYEE_OFFICE_ADDRESS_REQUIRED(1052, "Office employee office address field is required."),
    EMPLOYEE_EGN_BLANK(1053, "In case of employee update, egn field should not be blank."),
    EMPLOYEE_FIRST_NAME_BLANK(1054, "In case of employee update, first name field should not be blank."),
    EMPLOYEE_MIDDLE_NAME_BLANK(1055, "In case of employee update, middle name field should not be blank."),
    EMPLOYEE_LAST_NAME_BLANK(1056, "In case of employee update, last name field should not be blank."),
    OFFICE_EMPLOYEE_ADDRESS_BLANK(1057, "In case of office employee update, address field should not be blank."),
    VEHICLE_BRAND_REQUIRED(1058, "Vehicle brand field is required."),
    VEHICLE_MODEL_REQUIRED(1059, "Vehicle model field is required."),
    VEHICLE_REGISTRATION_PLATE_REQUIRED(1060, "Vehicle registration plate number field is required."),
    VEHICLE_BRAND_BLANK(1061, "In case of vehicle update, brand name field should not be blank."),
    VEHICLE_MODEL_BLANK(1062, "In case of vehicle update, model name field should not be blank."),
    VEHICLE_REGISTRATION_PLATE_BLANK(1063, "In case of vehicle update, registration plate number field should not be blank."),
    OFFICE_TELEPHONE_REQUIRED(1064, "Office telephone field is required."),
    OFFICE_TELEPHONE_BLANK(1065, "In case of vehicle update, office telephone field should not be blank."),
    SUPPLIER_EGN_REQUIRED(1066, "Supplier egn field is required."),
    SUPPLIER_FIRST_NAME_REQUIRED(1067, "Supplier first name field is required."),
    SUPPLIER_MIDDLE_NAME_REQUIRED(1068, "Supplier middle name field is required."),
    SUPPLIER_LAST_NAME_REQUIRED(1069, "Supplier last name field is required."),
    SUPPLIER_SALARY_REQUIRED(1070, "Supplier salary field is required."),
    SUPPLIER_EGN_BLANK(1071, "In case of supplier update, egn field should not be blank."),
    SUPPLIER_FIRST_NAME_BLANK(1072, "In case of supplier update, first name field should not be blank."),
    SUPPLIER_MIDDLE_NAME_BLANK(1073, "In case of supplier update, middle name field should not be blank."),
    SUPPLIER_LAST_NAME_BLANK(1074, "In case of supplier update, last name field should not be blank."),
    OFFICE_DATE_OF_ESTABLISHMENT_REQUIRED(1075, "Office date of establishment field is required."),
    TELEPHONE_INVALID_FORMAT(1076, "Telephone is not in a valid format."),
    EMPLOYEE_EMPLOY_DATE_REQUIRED(1077, "Employee employ date field is required."),
    EMPLOYEE_EGN_INVALID(1078, "Employee egn field is not a valid 10 digits length."),
    EMPLOYEE_SALARY_INVALID(1079, "Employee salary field is invalid, it should be greater than 0."),
    SUPPLIER_DRIVING_CATEGORIES_REQUIRED(1080, "Supplier driving categories field is required."),
    SUPPLIER_SALARY_INVALID(1081, "Supplier salary field is invalid, it should be greater than 0."),
    SHIPMENT_STATE_ALREADY_MOVED_TO_ARRIVED(1082, "Shipment state is already moved to arrived to recipient."),
    COMPANY_INCOME_PERIOD_START_DATE_REQ(1083, "Company income period should have start date."),
    COMPANY_INCOME_PERIOD_END_DATE_REQ(1084, "Company income period should have end date."),
    ADDRESS_IS_A_VALID_OFFICE_ERROR(1085, "The specified address is a valid company office address, please select office option or check your address again."),
    SUPPLIER_EMPLOY_DATE_REQUIRED(1086, "Supplier employ date field is required."),
    SUPPLIER_EGN_INVALID(1087, "Supplier egn field is not a valid 10 digits length."),
    SUPPLIER_VEHICLE_ID_REQUIRED(1088, "Supplier vehicle id field is required."),
    SHIPMENT_SENDER_SAME_AS_RECIPIENT__ERROR(1089, "Shipment cannot have sender and recipient with the same telephone numbers."),
    VEHICLE_CV(1090, "Vehicle with the specified id could not be deleted while there is an assigned supplier."),
    OFFICE_DATE_OF_ESTABLISHMENT_BLANK(1091, "In case of vehicle update, office date of establishment field should not be blank."),
    OFFICE_DATE_OF_ESTABLISHMENT_INVALID(1092, "Invalid office date of establishment."),
    OFFICE_EMPLOYEE_DATE_OF_ESTABLISHMENT_INVALID(1093, "Office employee date of establishment should be after office date if establishment."),
    USERNAME_REQUIRED(1094, "Username field is required."),
    PASSWORD_REQUIRED(1095, "Password field is required."),
    ROLE_NOT_FOUND(1096, "The specified role was not found."),
    USER_ALREADY_EXISTING( 1097, "User with the specified username already exists.");







    private final int code;
    private final String message;

    DomainErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

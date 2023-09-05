package com.semestrial_project.logistic_company.web.adapters;

import com.semestrial_project.logistic_company.domain.dto.account.CreateAccount;
import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.dto.office.CreateOffice;
import com.semestrial_project.logistic_company.domain.dto.office.OfficeResponse;
import com.semestrial_project.logistic_company.domain.dto.office.UpdateOffice;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeData;
import com.semestrial_project.logistic_company.domain.dto.office_employee.OfficeEmployeeResponse;
import com.semestrial_project.logistic_company.domain.dto.shipment.*;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierData;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.dto.user.CreateUser;
import com.semestrial_project.logistic_company.domain.dto.vehicle.CreateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.UpdateVehicle;
import com.semestrial_project.logistic_company.domain.dto.vehicle.VehicleResponse;
import com.semestrial_project.logistic_company.web.view.models.account.CreateAccountView;
import com.semestrial_project.logistic_company.web.view.models.office.CreateOfficeView;
import com.semestrial_project.logistic_company.web.view.models.office.OfficeViewResponse;
import com.semestrial_project.logistic_company.web.view.models.office.UpdateOfficeView;
import com.semestrial_project.logistic_company.web.view.models.office_employee.CreateOfficeEmployeeView;
import com.semestrial_project.logistic_company.web.view.models.office_employee.OfficeEmployeeViewResponse;
import com.semestrial_project.logistic_company.web.view.models.office_employee.UpdateOfficeEmployeeView;
import com.semestrial_project.logistic_company.web.view.models.shipment.*;
import com.semestrial_project.logistic_company.web.view.models.supplier.CreateSupplierView;
import com.semestrial_project.logistic_company.web.view.models.supplier.SupplierViewResponse;
import com.semestrial_project.logistic_company.web.view.models.supplier.UpdateSupplierView;
import com.semestrial_project.logistic_company.web.view.models.user.CreateEmployeeUserView;
import com.semestrial_project.logistic_company.web.view.models.user.CreateUserView;
import com.semestrial_project.logistic_company.web.view.models.vehicle.CreateVehicleView;
import com.semestrial_project.logistic_company.web.view.models.vehicle.UpdateVehicleView;
import com.semestrial_project.logistic_company.web.view.models.vehicle.VehicleViewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WebAdapter {

    OfficeViewResponse convertToOfficeViewResponse(OfficeResponse officeResponse);

    CreateOffice convertToCreateOffice(CreateOfficeView createOfficeView);

    UpdateOffice convertToUpdateOffice(UpdateOfficeView updateOfficeView);

    OfficeEmployeeViewResponse convertToOfficeEmployeeViewResponse(OfficeEmployeeResponse officeEmployeeResponse);

    OfficeEmployeeData convertToCreateOfficeEmployee(CreateOfficeEmployeeView createOfficeEmployeeView);

    OfficeEmployeeData convertToUpdateOfficeEmployee(UpdateOfficeEmployeeView updateOfficeEmployeeView);

    VehicleViewResponse convertToVehicleViewResponse(VehicleResponse vehicleResponse);

    CreateVehicle convertToCreateVehicle(CreateVehicleView createVehicleView);

    UpdateVehicle convertToUpdateVehicle(UpdateVehicleView updateVehicleView);

    @Mapping(target = "drivingLicenseCategories", source = "drivingCategories", qualifiedByName = "mapDrivingCategories")
    SupplierViewResponse convertToSupplierViewResponse(SupplierResponse officeEmployeeResponse, Set<DrivingCategoryResponse> drivingCategories);

    SupplierData convertToCreateSupplier(CreateSupplierView createSupplierView);

    SupplierData convertToUpdateSupplier(UpdateSupplierView updateSupplierView);

    @Named("mapDrivingCategories")
    default Set<String> map(Set<DrivingCategoryResponse> drivingCategoryResponses) {
        Set<String> categoryNames = new HashSet<>();
        drivingCategoryResponses.stream().map(DrivingCategoryResponse::getCategoryName).map(categoryNames::add).collect(Collectors.toList());
        return categoryNames;
    }

    ShipmentViewResponse convertToShipmentView(ShipmentResponse shipmentDto);

    CreateShipment convertToCreateShipment(CreateShipmentView createShipmentView);

    ProcessShipment convertToProcessShipment(ProcessShipmentView processShipmentView);

    UpdateShipment convertToUpdateShipment(UpdateShipmentView updateShipmentView);

    ChangeStatusShipment convertToChangeStatusShipment(ChangeStatusShipmentView changeStatusShipmentView);

    ShipmentStateViewResponse convertToShipmentStateViewResponse(ShipmentStateResponse shipmentStateResponse);

    CompanyIncome convertToShipmentIncome(CompanyIncomeView companyIncomeView);

    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "address.postCode", source = "postCode")
    @Mapping(target = "address.street", source = "street")
    @Mapping(target = "address.streetNum", source = "streetNum")
    CreateAccount convertToCreateAccount(CreateAccountView createAccountView);

    CreateUser convertToCreateEmployeeUser(CreateEmployeeUserView companyIncomeView);
}

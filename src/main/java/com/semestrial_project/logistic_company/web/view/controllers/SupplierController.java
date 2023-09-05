package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.dto.driving_category.DrivingCategoryResponse;
import com.semestrial_project.logistic_company.domain.dto.supplier.SupplierResponse;
import com.semestrial_project.logistic_company.domain.services.DrivingCategoryService;
import com.semestrial_project.logistic_company.domain.services.SupplierService;
import com.semestrial_project.logistic_company.domain.services.VehicleService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.supplier.CreateSupplierView;
import com.semestrial_project.logistic_company.web.view.models.supplier.SupplierViewResponse;
import com.semestrial_project.logistic_company.web.view.models.supplier.UpdateSupplierView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    private final DrivingCategoryService drivingCategoryService;

    private final VehicleService vehicleService;

    private final WebAdapter webAdapter;

    @GetMapping
    public String getAllSuppliers(Model model) {
        List<SupplierViewResponse> suppliers = supplierService.getAllSuppliers().stream()
                .map(supplier -> webAdapter.convertToSupplierViewResponse(supplier, supplier.getDrivingLicenseCategories())).collect(Collectors.toList());
        model.addAttribute("suppliers", suppliers);

        return "/suppliers/suppliers.html";
    }

    @GetMapping("/create-supplier")
    public String showCreateSupplierForm(Model model) {
        model.addAttribute("supplier", new CreateSupplierView());
        createSupplierModel(model);

        return "/suppliers/create-supplier";
    }

    @PostMapping("/create")
    public String createSupplier(@Valid @ModelAttribute("supplier") CreateSupplierView createSupplierView,
                                 BindingResult bindingResult,
                                 Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            createSupplierModel(model);
            return "/suppliers/create-supplier";
        }
        supplierService.createSupplier(webAdapter.convertToCreateSupplier(createSupplierView));

        return "redirect:/suppliers";
    }

    private void createSupplierModel(Model model) {
        model.addAttribute("drivingLicenseCategories", drivingCategoryService.getAllDrivingCategories()
                .stream().map(DrivingCategoryResponse::getCategoryName).collect(Collectors.toList()));
        model.addAttribute("vehicles", vehicleService.getAllVehicles()
                .stream().map(webAdapter::convertToVehicleViewResponse).collect(Collectors.toList()));
    }

    @GetMapping("/edit-supplier/{id}")
    public String showEditSupplierForm(Model model, @PathVariable Long id) throws Exception {
        SupplierResponse supplier = supplierService.getSupplierById(id);
        model.addAttribute("supplier", webAdapter
                .convertToSupplierViewResponse(supplier, supplier.getDrivingLicenseCategories()));
        model.addAttribute("drivingLicenseCategories", drivingCategoryService.getAllDrivingCategories()
                .stream().map(DrivingCategoryResponse::getCategoryName).collect(Collectors.toList()));
        model.addAttribute("vehicles", vehicleService.getAllVehicles()
                .stream().map(webAdapter::convertToVehicleViewResponse).collect(Collectors.toList()));

        return "/suppliers/edit-supplier";
    }

    @PatchMapping("/update/{id}")
    public String updateSupplier(@PathVariable Long id,
                                 @ModelAttribute("supplier") UpdateSupplierView updateSupplierView) throws Exception {
        supplierService.updateSupplier(id, webAdapter.convertToUpdateSupplier(updateSupplierView));

        return "redirect:/suppliers";
    }

    @GetMapping("/{id}/remove")
    public String removeSupplier(@PathVariable Long id) throws Exception {
        supplierService.removeSupplier(id);
        return "redirect:/suppliers";
    }
}

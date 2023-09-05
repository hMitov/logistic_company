package com.semestrial_project.logistic_company.web.view.controllers;

import com.semestrial_project.logistic_company.domain.services.OfficeEmployeeService;
import com.semestrial_project.logistic_company.domain.services.OfficeService;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.office_employee.CreateOfficeEmployeeView;
import com.semestrial_project.logistic_company.web.view.models.office_employee.OfficeEmployeeViewResponse;
import com.semestrial_project.logistic_company.web.view.models.office_employee.UpdateOfficeEmployeeView;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/office-employees")
public class OfficeEmployeeController {

    private final OfficeEmployeeService officeEmployeeService;

    private final OfficeService officeService;

    private final WebAdapter webAdapter;

    @GetMapping
    public String getAllOfficeEmployees(Model model) {
        List<OfficeEmployeeViewResponse> officeEmployees = officeEmployeeService.getAllOfficeEmployees().stream()
                .map(webAdapter::convertToOfficeEmployeeViewResponse).collect(Collectors.toList());
        model.addAttribute("officeEmployees", officeEmployees);

        return "/office-employees/office-employees.html";
    }

    @GetMapping("/create-office-employee")
    public String showCreateOfficeEmployeeForm(Model model) throws Exception {
        model.addAttribute("officeEmployee", new CreateOfficeEmployeeView());
        createOfficeEmployeeModel(model);

        return "/office-employees/create-office-employee";
    }

    @PostMapping("/create")
    public String createOfficeEmployee(@Valid @ModelAttribute("officeEmployee") CreateOfficeEmployeeView createOfficeEmployeeView,
                                       BindingResult bindingResult, Model model) throws Exception {
        if (bindingResult.hasErrors()) {
            createOfficeEmployeeModel(model);
            return "/office-employees/create-office-employee";
        }
        officeEmployeeService.createOfficeEmployee(webAdapter.convertToCreateOfficeEmployee(createOfficeEmployeeView));

        return "redirect:/office-employees";
    }
    private void createOfficeEmployeeModel(Model model) {
        model.addAttribute("offices", officeService.getAllOffices().stream()
                .map(webAdapter::convertToOfficeViewResponse).collect(Collectors.toList()));
    }

    @RequestMapping(path = "/office-employees/{id}", method = RequestMethod.GET)
    public OfficeEmployeeViewResponse getOfficeEmployee(@PathVariable Long id) throws Exception {
        return webAdapter.convertToOfficeEmployeeViewResponse(officeEmployeeService.getOfficeEmployeeById(id));
    }

    @GetMapping("/edit-office-employee/{id}")
    public String showEditOfficeEmployeeForm(Model model, @PathVariable Long id) throws Exception {
        OfficeEmployeeViewResponse officeEmployee = webAdapter
                .convertToOfficeEmployeeViewResponse(officeEmployeeService.getOfficeEmployeeById(id));
        model.addAttribute("officeEmployee", officeEmployee);
        model.addAttribute("offices", officeService.getAllOffices().stream()
                .map(webAdapter::convertToOfficeViewResponse).collect(Collectors.toList()));

        return "/office-employees/edit-office-employee";
    }

    @PatchMapping(value = "/update/{id}")
    public String updateOfficeEmployee(@PathVariable Long id,
                                       @ModelAttribute("office") UpdateOfficeEmployeeView updateOfficeEmployeeView) throws Exception {
        officeEmployeeService.updateOfficeEmployee(id, webAdapter.convertToUpdateOfficeEmployee(updateOfficeEmployeeView));

        return "redirect:/office-employees";
    }

    @GetMapping("/{id}/remove")
    public String removeOfficeEmployee(@PathVariable Long id) throws Exception {
        officeEmployeeService.removeOfficeEmployee(id);
        return "redirect:/office-employees";
    }
}

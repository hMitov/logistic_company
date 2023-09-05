package com.semestrial_project.logistic_company.web.view.controllers;


import com.semestrial_project.logistic_company.domain.entity.Role;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.RoleRepository;
import com.semestrial_project.logistic_company.domain.services.AccountService;
import com.semestrial_project.logistic_company.domain.services.implementations.UserDetailsServiceImpl;
import com.semestrial_project.logistic_company.web.adapters.WebAdapter;
import com.semestrial_project.logistic_company.web.view.models.account.CreateAccountView;
import com.semestrial_project.logistic_company.web.view.models.user.CreateEmployeeUserView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/")
public class IndexController {

    private final UserDetailsServiceImpl userDetailsService;
    private final RoleRepository roleRepository;
    private final AccountService accountService;
    private final WebAdapter webAdapter;

    @GetMapping
    public String getIndex(Model model) {
        final String welcomeMessage = "Welcome to Logistic company Speeed!";
        model.addAttribute("welcome", welcomeMessage);
        return "index";
    }

    @GetMapping("login")
    public String login(Model model) {
        final String welcomeMessage = "Welcome to Logistic company Speeed!";
        model.addAttribute("welcome", welcomeMessage);
        return "login";
    }

    @GetMapping("register")
    public String registerAccount(Model model) {
        final String welcomeMessage = "Welcome to the Logistic company Speeed!";
        model.addAttribute("welcome", welcomeMessage);
        model.addAttribute("account", new CreateAccountView());
        return "register";
    }

    @GetMapping("user/register")
    public String registerUser(Model model) {
        final String welcomeMessage = "Welcome to the Medical Record Service!";
        getRolesLoaded(model);

        model.addAttribute("welcome", welcomeMessage);
        model.addAttribute("user", new CreateEmployeeUserView());

        return "user-register";
    }

    private void getRolesLoaded(Model model) {
        List<String> rolesLoaded = roleRepository.findAll().stream().map(Role::getName)
                .filter(name -> !name.equals("CUSTOMER")).collect(Collectors.toList());

        model.addAttribute("roles", rolesLoaded);
    }

    @PostMapping("register")
    public String createAccount(@Valid @ModelAttribute("account") CreateAccountView createAccountView,
                                BindingResult bindingResult) throws DomainException {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        accountService.createAccount(webAdapter.convertToCreateAccount(createAccountView));
        return "login";
    }

    @PostMapping("user/register")
    public String createUser(@Valid @ModelAttribute("user") CreateEmployeeUserView createUserView,
                             BindingResult bindingResult,
                             Model model) throws DomainException {
        if (bindingResult.hasErrors()) {
            getRolesLoaded(model);
            return "user-register";
        }
        userDetailsService.createUser(webAdapter.convertToCreateEmployeeUser(createUserView));
        return "fragments";
    }

    @GetMapping("logout")
    public String logout(Model model) {
        final String welcomeMessage = "Welcome to Logistic company Speeed!";
        model.addAttribute("welcome", welcomeMessage);
        return "login";
    }

    @GetMapping("unauthorized")
    public String unauthorized(Model model) {
        final String welcomeMessage = "Welcome to Logistic company Speeed!";
        model.addAttribute("welcome", welcomeMessage);
        return "unauthorized";
    }
}
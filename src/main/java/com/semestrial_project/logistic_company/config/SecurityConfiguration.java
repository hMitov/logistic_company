package com.semestrial_project.logistic_company.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register").permitAll()

                .antMatchers(HttpMethod.GET, "/user/register").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/user/register").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/offices").permitAll()
                .antMatchers(HttpMethod.GET, "/offices/create-office").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/offices/create").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/offices/edit-office/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/offices/update/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/offices/{id}/remove").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/office-employees").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/office-employees/create-office-employee").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/office-employees/create").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/office-employees/edit-office-employee/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/office-employees/update/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/office-employees/{id}/remove").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/suppliers").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/suppliers/create-supplier").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/suppliers/create").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/suppliers/edit-supplier/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/suppliers/update/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/suppliers/{id}/remove").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/vehicles").hasAnyAuthority("ADMIN", "SUPPLIER", "OFFICE_EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/vehicles/create-vehicle").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/vehicles/create").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/vehicles/edit-vehicle/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/vehicles/update/{id}").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/vehicles/{id}/remove").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.GET, "/shipments/transport-ready").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/shipments/create-shipment").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "CUSTOMER")
                .antMatchers(HttpMethod.POST, "/shipments/create").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "CUSTOMER")
                .antMatchers(HttpMethod.GET, "/shipments/shipment-process/{id}").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.POST, "/shipments/process/{id}").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/shipments/shipment-change-status/{id}").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.POST, "/shipments/change-status/{id}").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/shipments/in-transport").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/shipments/process").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/shipments/{id}/remove").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE")

                .antMatchers(HttpMethod.GET, "/reference/shipments").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/reference/shipments-by-employee").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/reference/shipments/registered-by-office-employee").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE")
                .antMatchers(HttpMethod.GET, "/reference/shipments-by-sender").permitAll()
                .antMatchers(HttpMethod.GET, "/reference/shipments/sent-by-sender").permitAll()
                .antMatchers(HttpMethod.GET, "/reference/shipments/sent").hasAnyAuthority("ADMIN", "OFFICE_EMPLOYEE", "SUPPLIER")
                .antMatchers(HttpMethod.GET, "/reference/shipments-by-recipient").permitAll()
                .antMatchers(HttpMethod.GET, "/reference/shipments/received-by-recipient").permitAll()
                .antMatchers(HttpMethod.GET, "/reference/shipments/employees").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/reference/income-for-period").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/reference/income").hasAnyAuthority("ADMIN")

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and();
    }
}

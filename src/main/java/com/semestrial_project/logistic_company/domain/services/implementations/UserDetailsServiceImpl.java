package com.semestrial_project.logistic_company.domain.services.implementations;

import com.semestrial_project.logistic_company.domain.dto.user.CreateUser;
import com.semestrial_project.logistic_company.domain.entity.Role;
import com.semestrial_project.logistic_company.domain.entity.User;
import com.semestrial_project.logistic_company.domain.exceptions.DomainException;
import com.semestrial_project.logistic_company.domain.repository.RoleRepository;
import com.semestrial_project.logistic_company.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.semestrial_project.logistic_company.domain.exceptions.code.DomainErrorCode.*;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final String USER_NOT_FOUND_ERROR = "User with the specified username was not found.";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR));
    }

    public void createUser(CreateUser createUser) throws DomainException {
        Optional<User> userLoaded = userRepository.findUserByUsername(createUser.getUsername());
        if(userLoaded.isPresent()) {
            throw new DomainException(USER_ALREADY_EXISTING);
        }
        User user = User.builder()
                .username(createUser.getUsername())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .build();

        if (Objects.nonNull(createUser.getRoles())) {
            Set<Role> roleSet = new HashSet<>();
            for (String role : createUser.getRoles()) {
                roleSet.add(roleRepository.findByName(role).orElseThrow(() -> new DomainException(ROLE_NOT_FOUND)));
            }
            user = user.toBuilder()
                    .roles(roleSet)
                    .build();
        } else {
            Optional<Role> role = roleRepository.findById(Role.CUSTOMER);
            user = user.toBuilder()
                    .roles(Set.of(role.get()))
                    .build();
        }

        userRepository.save((User) user);
    }
}

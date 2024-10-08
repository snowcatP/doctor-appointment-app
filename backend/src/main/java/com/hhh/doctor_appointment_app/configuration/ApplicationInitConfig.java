package com.hhh.doctor_appointment_app.configuration;

import com.hhh.doctor_appointment_app.dto.request.UserCreateRequest;
import com.hhh.doctor_appointment_app.entity.Role;
import com.hhh.doctor_appointment_app.enums.UserRole;
import com.hhh.doctor_appointment_app.repository.AdminRepository;
import com.hhh.doctor_appointment_app.repository.RoleRepository;
import com.hhh.doctor_appointment_app.repository.UserRepository;
import com.hhh.doctor_appointment_app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationInitConfig implements ApplicationRunner{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;

    ApplicationInitConfig(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserService userService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        for (UserRole role : UserRole.values()) {
            if (!roleRepository.existsByRoleName(role)) {
                roleRepository.save(Role.builder().roleName(role).build());
            }
        }

        if (!userRepository.existsByUsername("admin@gmail.com")) {
            UserCreateRequest request = UserCreateRequest.builder()
                    .email("admin@gmail.com")
                    .password("Hello@123")
                    .phone("0000000000")
                    .address("HCMC")
                    .firstName("Admin")
                    .lastName("Admin")
                    .gender(true)
                    .build();
            var result = userService.createAdmin(request);
            if (result != null) {
                log.info("Admin account created with: username=admin@gmail.com, password=Hello@123");
            } else {
                log.info("Can't create admin account");
            }

        }
    }
}

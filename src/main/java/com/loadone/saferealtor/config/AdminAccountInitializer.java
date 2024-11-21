package com.loadone.saferealtor.config;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.SignupType;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {

    @Value("${admin.userid}")
    private String adminUser;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.phone-number}")
    private String adminPhoneNumber;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Admin 계정이 없으면 새로 추가
        if (userRepository.findByUserId(adminUser).isEmpty()) {

            User admin = User.builder()
                    .userId(adminUser)
                    .name("관리자")
                    .password(passwordEncoder.encode(adminPassword))
                    .signupType(SignupType.ADMIN)
                    .role(Role.ROLE_ADMIN)
                    .phoneNumber(adminPhoneNumber)
                    .build();

            userRepository.save(admin);
            log.info(" Admin account created. :: "+adminUser);
        } else {
            log.info(" Admin account already exists. :: "+adminUser);
        }
    }
}

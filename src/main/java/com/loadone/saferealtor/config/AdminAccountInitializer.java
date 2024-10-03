package com.loadone.saferealtor.config;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
            User admin = new User();
            admin.setUserId("admin");
            admin.setPassword(passwordEncoder.encode(adminPassword));  // 기본 패스워드 설정
            admin.setRole(Role.ROLE_ADMIN); // 관리자 권한 부여
            admin.setPhoneNumber(adminPhoneNumber);
            userRepository.save(admin);
            log.info("Admin account created. :: "+adminUser);
        } else {
            log.info("Admin account already exists. :: "+adminUser);
        }
    }
}

package com.loadone.saferealtor.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.loadone.saferealtor.model.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void TestInsert(){

        for(int i=1; i<=10; i++){

            User user = new User();
            user.setUserId("user" + i);
            user.setPassword(passwordEncoder.encode("1234"));
            user.setPhoneNumber("010-1234-567"+i);
            user.setRole(User.ROLE_USER);

            userRepository.save(user);

        }
    }

}
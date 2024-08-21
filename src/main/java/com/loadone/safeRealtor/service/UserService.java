package com.loadone.safeRealtor.service;

import com.loadone.safeRealtor.model.entity.User;
import com.loadone.safeRealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 사용자명으로 사용자 조회
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // 사용자 정보 수정
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }
}

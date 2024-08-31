package com.loadone.saferealtor.service;

import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 사용자명으로 사용자 조회
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 사용자 정보 수정
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow();
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }
}

package com.loadone.saferealtor.service;

import com.loadone.saferealtor.exception.BaseException;
import com.loadone.saferealtor.exception.ErrorCode;
import com.loadone.saferealtor.model.dto.UserDTO;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 사용자명으로 사용자 조회
    public User findUserByUserId(String userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));
    }

    // 사용자 정보 수정
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getUserSeq()).orElseThrow();
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(existingUser);
    }

    // 역할별 사용자 목록 조회
    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }
}

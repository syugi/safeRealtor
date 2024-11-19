package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.dto.UserDTO;
import com.loadone.saferealtor.model.entity.Role;
import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /* 사용자 정보 조회 */
    @GetMapping("/{userId}")
    public User getUserByUserId(@PathVariable String userId) {
        return userService.findUserByUserId(userId);
    }

    /* 사용자 정보 수정 */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long userSeq, @RequestBody User user) {
        user.setUserSeq(userSeq);
        return userService.updateUser(user);
    }

    // 관리자 목록 조회
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAdminList() {
        List<UserDTO> admins = userService.getUsersByRole(Role.ROLE_ADMIN);
        return ResponseEntity.ok(admins);
    }

    // 중개사 목록 조회
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_AGENT')")
    @GetMapping("/agents")
    public ResponseEntity<List<UserDTO>> getAgentList() {
        List<UserDTO> agents = userService.getUsersByRole(Role.ROLE_AGENT);
        return ResponseEntity.ok(agents);
    }

    // 일반 회원 목록 조회
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_AGENT')")
    @GetMapping("/members")
    public ResponseEntity<List<UserDTO>> getMemberList() {
        List<UserDTO> members = userService.getUsersByRole(Role.ROLE_USER);
        return ResponseEntity.ok(members);
    }
}

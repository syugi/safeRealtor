package com.loadone.saferealtor.controller;

import com.loadone.saferealtor.model.entity.User;
import com.loadone.saferealtor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }
}

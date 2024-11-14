package com.delivery_project.controller.api;

import com.delivery_project.dto.request.UpdateUserRequestDto;
import com.delivery_project.dto.UserInfoDto;
import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.repository.UserRepository;
import com.delivery_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseEntity.ok("Registration Complete");
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String username) {
        UserInfoDto userInfo = userService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserInfoDto> updateUserInfo(
            @PathVariable String username,
            @RequestBody UpdateUserRequestDto updateRequest) {
        UserInfoDto updatedUser = userService.updateUserInfo(username, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<Void> deactivateUser(@PathVariable String username) {
        userService.deactivateUser(username);
        return ResponseEntity.noContent().build();
    }
}

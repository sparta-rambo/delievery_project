package com.delivery_project.controller.api;

import com.delivery_project.dto.request.UpdateUserRequestDto;
import com.delivery_project.dto.UserInfoDto;
import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.repository.jpa.UserRepository;
import com.delivery_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

    private final UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        MessageResponseDto responseDto = new MessageResponseDto("Registration Complete");
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String username) {
        UserInfoDto userInfo = userService.getUserInfo(username);
        return ResponseEntity.ok(userInfo);
    }

    @PutMapping("/{username}")
    public ResponseEntity<MessageResponseDto> updateUserInfo(
            @PathVariable String username,
            @RequestBody UpdateUserRequestDto updateRequest) {
        MessageResponseDto responseMessage = userService.updateUser(username, updateRequest);
        return ResponseEntity.ok(responseMessage);
    }

    @PatchMapping("/{username}")
    public ResponseEntity<MessageResponseDto> deleteUser(@PathVariable String username) {
        MessageResponseDto responseMessage = userService.deleteUser(username);
        return ResponseEntity.ok(responseMessage);
    }
}

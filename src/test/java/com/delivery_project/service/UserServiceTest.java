package com.delivery_project.service;

import static org.mockito.Mockito.*;

import com.delivery_project.controller.api.UserRestController;
import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // MockitoAnnotations.openMocks(this);  // @ExtendWith(MockitoExtension.class) 로 대체
    }

    @Test
    @DisplayName("회원가입 테스트")
    void signup_successful() {
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .username("testuser")
                .password("Password1!")
                .address("Test Address")
                .role(UserRoleEnum.valueOf("CUSTOMER"))
                .build();

        // Mock 처리
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");

        // 테스트 유저 생성
        User mockUser = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .password("encodedPassword")
                .address("Test Address")
                .role(UserRoleEnum.CUSTOMER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // ArgumentCaptor를 사용하여 save 메서드에 전달된 User 객체 캡처
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        // 회원가입 실행
        userService.signup(signupRequestDto);

        // verify 메서드로 호출 확인
        verify(userRepository, times(1)).findByUsername("testuser"); // Username check
        verify(userRepository, times(1)).save(userCaptor.capture()); // Save user check
        verify(passwordEncoder, times(1)).encode("Password1!"); // Password encoding check

        // 캡처된 User 객체 로그 출력
        User capturedUser = userCaptor.getValue();
        System.out.println("Captured User: " + capturedUser); // 콘솔에 출력

        // 세부 정보 로그 출력
        System.out.println("Captured User Details:");
        System.out.println("Username: " + capturedUser.getUsername());
        System.out.println("Password: " + capturedUser.getPassword());
        System.out.println("Address: " + capturedUser.getAddress());
        System.out.println("Role: " + capturedUser.getRole());
    }
}

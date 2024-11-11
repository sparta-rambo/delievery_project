package com.delivery_project.service;

import com.delivery_project.dto.request.SigninRequestDto;
import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.jwt.JwtUtil;
import com.delivery_project.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // MANAGER_TOKEN
    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        String address = signupRequestDto.getAddress();

        // 관리자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        if (signupRequestDto.isManager()) {
            if (!MANAGER_TOKEN.equals(signupRequestDto.getManagerToken())) {
                throw new IllegalArgumentException("MANAGER 토큰이 유효하지 않습니다.");
            }
            role = UserRoleEnum.MANAGER;
        }

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(password)
                .address(address)
                .role(role)
                .build();

        userRepository.save(user);

    }

    public void signin(SigninRequestDto signinRequestDto, HttpServletResponse response) {
        String username = signinRequestDto.getUsername();
        String password = signinRequestDto.getPassword();

        // username 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // password 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        jwtUtil.addJwtToCookie(token, response);
    }
}

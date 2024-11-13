package com.delivery_project.service;

import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.UserRepository;
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

    // TOKEN
    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    private final String MASTER_TOKEN = "AAC9R5zWXkZfy1DpL3bKuRAd7fHbCe9qe";

    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        String address = signupRequestDto.getAddress();

        // ROLE 확인
        UserRoleEnum role = signupRequestDto.getRole();
        if (role == UserRoleEnum.MANAGER) {
            if (!MANAGER_TOKEN.equals(signupRequestDto.getManagerToken())) {
                throw new IllegalArgumentException("MANAGER 토큰이 유효하지 않습니다.");
            }
            role = UserRoleEnum.MANAGER;
        } else if (role == UserRoleEnum.MASTER) {
            if (!MASTER_TOKEN.equals(signupRequestDto.getMasterToken())) {
                throw new IllegalArgumentException("MASTER 토큰이 유효하지 않습니다.");
            }
            role = UserRoleEnum.MANAGER;
        } else {
            role = UserRoleEnum.CUSTOMER;
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
}

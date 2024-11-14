package com.delivery_project.service;

import com.delivery_project.dto.request.UpdateUserRequestDto;
import com.delivery_project.dto.UserInfoDto;
import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.SuccessMessage;
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
        if (!username.matches("^[a-z0-9]{4,10}$")) {
            throw new IllegalArgumentException("아이디는 4자 이상 10자 이하이며, 알파벳 소문자와 숫자로만 구성되어야 합니다.");
        }

        String password = signupRequestDto.getPassword();
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")) {
            throw new IllegalArgumentException("비밀번호는 8자 이상 15자 이하이며, 알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.");
        }
        password = passwordEncoder.encode(password);

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        String address = signupRequestDto.getAddress();

        // ROLE 확인 및 유효성 검사
        UserRoleEnum role = signupRequestDto.getRole();
        if (role == UserRoleEnum.MANAGER) {
            if (!MANAGER_TOKEN.equals(signupRequestDto.getManagerToken())) {
                throw new IllegalArgumentException("유효하지 않은 MANAGER 토큰입니다.");
            }
            role = UserRoleEnum.MANAGER;
        } else if (role == UserRoleEnum.MASTER) {
            if (!MASTER_TOKEN.equals(signupRequestDto.getMasterToken())) {
                throw new IllegalArgumentException("유효하지 않은 MASTER 토큰입니다.");
            }
            role = UserRoleEnum.MANAGER;
        } else if (role != UserRoleEnum.CUSTOMER && role != UserRoleEnum.OWNER) {
            throw new IllegalArgumentException("존재하지 않는 role입니다.");
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

    public UserInfoDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserInfoDto(user);
    }

    public MessageResponseDto updateUser(String username, UpdateUserRequestDto updateRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (updateRequest.getRole() == UserRoleEnum.MANAGER) {
            if (updateRequest.getManagerToken() == null) {
                throw new IllegalArgumentException("MANAGER 토큰을 입력해야 합니다.");
            }
            if (!MANAGER_TOKEN.equals(updateRequest.getManagerToken())) {
                throw new IllegalArgumentException("유효하지 않은 MANAGER 토큰입니다.");
            }
        } else if (updateRequest.getRole() == UserRoleEnum.MASTER) {
            if (updateRequest.getMasterToken() == null) {
                throw new IllegalArgumentException("MASTER 토큰을 입력해야 합니다.");
            }
            if (!MASTER_TOKEN.equals(updateRequest.getMasterToken())) {
                throw new IllegalArgumentException("유효하지 않은 MASTER 토큰입니다.");
            }
        }

        User updatedUser = user.toBuilder()
                .id(user.getId())
                .username(updateRequest.getUsername() != null ? updateRequest.getUsername() : user.getUsername())
                .address(updateRequest.getAddress() != null ? updateRequest.getAddress() : user.getAddress())
                .role(updateRequest.getRole() != null ? updateRequest.getRole() : user.getRole())
                .isDeleted(user.isDeleted())
                .build();

        userRepository.save(updatedUser);
        return new MessageResponseDto("User" + SuccessMessage.UPDATE.getMessage());
    }

    public MessageResponseDto deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        User updatedUser = user.toBuilder()
                .isDeleted(true)
                .build();

        userRepository.save(updatedUser);
        return new MessageResponseDto("User" + SuccessMessage.DELETE.getMessage());
    }
}

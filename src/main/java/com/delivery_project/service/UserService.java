package com.delivery_project.service;

import com.delivery_project.dto.request.SignupRequestDto;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // MANAGER_TOKEN
    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        String address = requestDto.getAddress();

        // 관리자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.CUSTOMER;
        if (requestDto.isManager()){
            if (!MANAGER_TOKEN.equals(requestDto.getManagerToken())) {
                throw new IllegalArgumentException("MANAGER token is not valid");
            }
            role = UserRoleEnum.MANAGER;
        }

        User user = new User(username, password, address, role);
        userRepository.save(user);

        //System.out.println("Username: " + username);

    }
}

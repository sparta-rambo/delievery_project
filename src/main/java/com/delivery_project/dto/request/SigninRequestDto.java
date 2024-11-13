package com.delivery_project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SigninRequestDto {
    private String username;
    private String password;

    public SigninRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
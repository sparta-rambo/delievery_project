package com.delivery_project.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String username;
    private String password;
    private String address;
    private boolean admin = false;
    private String adminToken = "";
}
package com.delivery_project.dto.request;

import com.delivery_project.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {
    private String username;
    private String password;
    private String address;
    private UserRoleEnum role;
    private String managerToken = "";
    private String masterToken = "";
}
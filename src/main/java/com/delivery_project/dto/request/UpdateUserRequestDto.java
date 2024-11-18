package com.delivery_project.dto.request;

import com.delivery_project.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateUserRequestDto {
    private String username;
    private String address;
    private UserRoleEnum role;
    private String managerToken = "";
    private String masterToken = "";
}

package com.delivery_project.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoDto {
    private String username;
    private String address;
    private boolean isManager;
}

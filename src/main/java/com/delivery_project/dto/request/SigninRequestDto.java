package com.delivery_project.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninRequestDto {
    private String username;
    private String password;
}

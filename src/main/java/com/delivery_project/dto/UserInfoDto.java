package com.delivery_project.dto;

import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String username;
    private String address;
    private UserRoleEnum role;

    public UserInfoDto(User user) {
        this.username = user.getUsername();
        this.address = user.getAddress();
        this.role = user.getRole();
    }
}

package com.delivery_project.dto.request;

import lombok.Getter;

public class TestRequestDto {

    @Getter
    public static class Create{
        String username;
        String password;
        String role;

        public Create(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
}

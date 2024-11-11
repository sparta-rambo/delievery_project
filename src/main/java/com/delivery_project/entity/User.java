package com.delivery_project.entity;

import com.delivery_project.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User extends Timestamped{
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false, length = 255)
    private String address;

    public User(String username, String password, String address, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.role = role;
    }
}
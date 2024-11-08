package com.delivery_project.entity;

import com.delivery_project.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "p_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 20)
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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = UserRoleEnum.CUSTOMER;
        this.address = "";
    }
}
package com.delivery_project.entity;

import com.delivery_project.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "p_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class User extends Timestamped{
    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private String address;

    private boolean isDeleted = false;

    public User(String username, String address, UserRoleEnum role, boolean isDeleted) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.address = address;
        this.role = role;
        this.isDeleted = isDeleted;
    }
}
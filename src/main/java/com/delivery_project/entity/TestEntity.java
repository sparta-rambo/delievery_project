package com.delivery_project.entity;

import com.delivery_project.dto.request.TestRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_users")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    private String role;

    public TestEntity(TestRequestDto.Create testRequestDto) {
        this.username = testRequestDto.getUsername();
        this.password = testRequestDto.getPassword();
        this.role = testRequestDto.getRole();
    }
}


package com.delivery_project.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Category extends Timestamped {

    @Id
    private UUID id;

    @Column(nullable = false, length = 50, unique = true)
    private String name;

}

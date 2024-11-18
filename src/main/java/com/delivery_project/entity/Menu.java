package com.delivery_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_menus")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Menu extends Timestamped {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Boolean isHidden = false;

    // 소프트 삭제 및 숨김 처리를 위한 메서드
    public void markAsDeleted(String deletedBy) {
        this.delete(deletedBy); // Timestamped의 delete 메서드 호출
        this.isHidden = true;
    }
}

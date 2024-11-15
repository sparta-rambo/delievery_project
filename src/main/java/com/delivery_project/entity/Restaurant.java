package com.delivery_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_restaurants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Restaurant extends Timestamped {

    @Id
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private Boolean isHidden = false;

    @Transient // DB에 저장하지는 않고 평점 계산용으로 사용
    private Double averageRating;

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    // 소프트 삭제 및 숨김 처리를 위한 메서드
    public void markAsDeleted(String deletedBy) {
        this.delete(deletedBy); // Timestamped의 delete 메서드 호출
        this.isHidden = true;
    }
}


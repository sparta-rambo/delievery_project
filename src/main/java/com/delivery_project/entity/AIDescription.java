package com.delivery_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_ai_descriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AIDescription extends Timestamped{

    @Id
    private UUID id;

    @Column(nullable = false, length = 1000)
    private String aiRequest;

    @Column(nullable = false, length = 1000)
    private String aiResponse;

    @Column(nullable = false)
    private LocalDateTime createdAt;  // 응답 생성 시간

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}

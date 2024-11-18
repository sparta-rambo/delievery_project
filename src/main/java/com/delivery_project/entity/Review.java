package com.delivery_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_reviews")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Review extends Timestamped{

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    public void update(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

}


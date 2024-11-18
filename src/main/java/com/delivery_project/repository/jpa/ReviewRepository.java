package com.delivery_project.repository.jpa;

import com.delivery_project.entity.Review;
import com.delivery_project.repository.implement.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewRepositoryCustom {
}

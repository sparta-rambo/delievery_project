package com.delivery_project.repository.jpa;

import com.delivery_project.entity.AIDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AIDescriptionRepository extends JpaRepository<AIDescription, UUID> {
}

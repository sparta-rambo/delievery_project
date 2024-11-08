package com.delivery_project.service;

import com.delivery_project.entity.Category;
import com.delivery_project.exception.DuplicateCategoryNameException;
import com.delivery_project.repository.jpa.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void addCategory(String categoryName) {
        try {
            // Builder를 사용하여 Category 객체 생성
            Category category = Category.builder()
                .id(UUID.randomUUID())
                .name(categoryName)
                .build();

            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateCategoryNameException("이미 존재하는 카테고리 이름입니다.");
        }
    }
}

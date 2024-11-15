package com.delivery_project.service;

import com.delivery_project.common.exception.BadRequestException;
import com.delivery_project.common.exception.DuplicateResourceException;
import com.delivery_project.common.exception.InvalidInputException;
import com.delivery_project.common.exception.ResourceNotFoundException;
import com.delivery_project.dto.response.CategoryResponseDto;
import com.delivery_project.entity.Category;
import com.delivery_project.entity.User;
import com.delivery_project.enums.UserRoleEnum;
import com.delivery_project.repository.jpa.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 권한 검증
    private void validateUserAccess(User user) {
        if (!(user.getRole().equals(UserRoleEnum.MANAGER) || user.getRole()
            .equals(UserRoleEnum.MASTER))) {
            throw new BadRequestException("접근권한이 없습니다.");
        }
    }

    public void addCategory(String categoryName, User user) {

        validateUserAccess(user);

        if (categoryRepository.existsByName(categoryName)) {
            throw new DuplicateResourceException("이미 존재하는 카테고리 이름입니다.");
        }

        // 카테고리 객체 생성 후 저장
        Category category = Category.builder()
            .id(UUID.randomUUID())
            .name(categoryName)
            .build();

        categoryRepository.save(category);
    }

    public void updateCategory(UUID categoryId, String categoryName, User user) {

        validateUserAccess(user);

        if (categoryRepository.existsByName(categoryName)) {
            throw new DuplicateResourceException("이미 존재하는 카테고리 이름입니다.");
        }

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NullPointerException("존재하지 않는 카테고리입니다."));

        Category newCategory = Category.builder().id(categoryId).name(categoryName).build();

        categoryRepository.save(newCategory);
    }

    public List<CategoryResponseDto> getAllCategory() {

        List<Category> categoryList = categoryRepository.findAll();

        if (categoryList.isEmpty()) {
            throw new ResourceNotFoundException("카테고리 데이터가 없습니다.");
        }

        List<CategoryResponseDto> categoryResponseDtoList = categoryList.stream()
            .map(category -> CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build())
            .collect(Collectors.toList());

        return categoryResponseDtoList;
    }
}

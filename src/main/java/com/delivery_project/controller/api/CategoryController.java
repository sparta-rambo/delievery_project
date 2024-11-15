package com.delivery_project.controller.api;

import com.delivery_project.dto.request.CategoryRequestDto;
import com.delivery_project.dto.response.CategoryResponseDto;
import com.delivery_project.dto.response.MessageResponseDto;
import com.delivery_project.enums.SuccessMessage;
import com.delivery_project.security.UserDetailsImpl;
import com.delivery_project.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<?> addCategory(
        @Valid @RequestBody CategoryRequestDto categoryRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String categoryName = categoryRequestDto.getName();
        categoryService.addCategory(categoryName, userDetails.getUser());

        return ResponseEntity.ok(
            new MessageResponseDto("Category" + SuccessMessage.CREATE.getMessage()));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(
        @PathVariable("categoryId") UUID categoryId,
        @Valid @RequestBody CategoryRequestDto categoryRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        String categoryName = categoryRequestDto.getName();
        categoryService.updateCategory(categoryId, categoryName, userDetails.getUser());

        return ResponseEntity.ok(
            new MessageResponseDto("Category" + SuccessMessage.UPDATE.getMessage()));

    }

    @GetMapping()
    public List<CategoryResponseDto> getAllCategory() {

        return categoryService.getAllCategory();
    }

}

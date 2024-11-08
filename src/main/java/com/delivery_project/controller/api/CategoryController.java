package com.delivery_project.controller.api;

import com.delivery_project.dto.request.CategoryRequestDto;
import com.delivery_project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/")
    public void addCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
        String categoryName = categoryRequestDto.getName();
        categoryService.addCategory(categoryName);
    }
}

package com.signal.signalbe.api.category;

import com.signal.signalbe.domain.category.Category;
import com.signal.signalbe.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findByParentIsNullOrderBySortOrder().stream()
                .filter(Category::isActive)
                .map(CategoryResponse::from)
                .toList();
    }
}

package org.example.bookstore.util;

import java.util.List;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.example.bookstore.model.Category;

public class CategoryUtil {
    public static Category createCategory(Long id) {
        Category category = new Category();
        category.setId(id);
        category.setName("Category name");
        category.setDescription("Category description");
        return category;
    }

    public static CategoryDto mapCategoryDtoToCategory(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Category name");
        categoryRequestDto.setDescription("Category description");
        return categoryRequestDto;
    }

    public static CategoryDto createCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Category name");
        categoryDto.setDescription("Category description");
        return categoryDto;
    }

    public static List<CategoryDto> createListOfCategoryDtos() {
        CategoryDto firstDto = new CategoryDto();
        firstDto.setId(1L);
        firstDto.setName("FIRST CATEGORY");
        firstDto.setDescription("first description");
        CategoryDto secondDto = new CategoryDto();
        secondDto.setId(2L);
        secondDto.setName("SECOND CATEGORY");
        secondDto.setDescription("second description");
        return List.of(firstDto, secondDto);
    }
}

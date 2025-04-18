package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.example.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryDto);

    void updateCategoryFromDto(CategoryRequestDto requestDto, @MappingTarget Category category);
}

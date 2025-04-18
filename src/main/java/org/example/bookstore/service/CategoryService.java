package org.example.bookstore.service;

import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);
}

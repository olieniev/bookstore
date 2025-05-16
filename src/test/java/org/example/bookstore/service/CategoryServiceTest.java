package org.example.bookstore.service;

import static org.example.bookstore.util.CategoryUtil.createCategory;
import static org.example.bookstore.util.CategoryUtil.createCategoryRequestDto;
import static org.example.bookstore.util.CategoryUtil.mapCategoryDtoToCategory;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("""
        Get category by id with existing id returns CategoryDto
            """)
    public void getBookById_correctId_returnsBookDto() {
        Long categoryId = 1L;
        Category category = createCategory(categoryId);
        CategoryDto expected = mapCategoryDtoToCategory(category);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.getById(categoryId);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("""
        Get category by id with invalid id throws EntityNotFoundException
            """)
    public void getBookById_invalidId_throwsException() {
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("""
        Save valid category returns categoryDto
            """)
    public void save_validBook_returnsBookDto() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createCategoryRequestDto();
        Category category = createCategory(categoryId);
        CategoryDto expected = mapCategoryDtoToCategory(category);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(requestDto);
        Assertions.assertEquals(expected, actual);
        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("""
        Update valid book returns BookDto
            """)
    public void update_validBook_returnsBookDto() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createCategoryRequestDto();
        Category category = createCategory(categoryId);
        CategoryDto expected = mapCategoryDtoToCategory(category);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(requestDto, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.update(categoryId, requestDto);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(requestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("""
        Update invalid book throws EntityNotFoundException
            """)
    public void update_inValidBook_throwsException() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = createCategoryRequestDto();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, requestDto));
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("""
        Delete book by id returns void
            """)
    public void delete_bookById_returnsVoid() {
        Long categoryId = 888L;
        doNothing().when(categoryRepository).deleteById(categoryId);
        categoryService.deleteById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("""
        Find all books returns page of BookDto
            """)
    public void find_allBooks_returnsBookDtos() {
        Pageable pageable = PageRequest.of(0, 1);
        Category category = createCategory(3L);
        CategoryDto categoryDto = mapCategoryDtoToCategory(category);
        Page<Category> pageOfCategory = new PageImpl<>(List.of(category), pageable, 1);
        Page<CategoryDto> expected = new PageImpl<>(List.of(categoryDto), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(pageOfCategory);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> actual = categoryService.findAll(pageable);
        Assertions.assertEquals(expected, actual);
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }
}

package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.example.bookstore.service.BookService;
import org.example.bookstore.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Controller for Category class",
        description = "All endpoints of Category class")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create category method",
            description = "Creates a category with given parameters")
    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get all categories method",
            description = "Returns a list of all categories")
    @GetMapping
    public Page<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get category by id method",
            description = "Returns a category by provided id")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category by id method",
            description = "Updates a category by provided id and request body")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category method",
            description = "Deletes a category with given id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get books by category id method",
            description = "Returns books by provided category id")
    @GetMapping("/{id}/books")
    public Page<BookDtoWithoutCategoryIds> getBooksByCategoryId(
            @PathVariable Long id, Pageable pageable
    ) {
        return bookService.getByCategoriesId(id, pageable);
    }
}

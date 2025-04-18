package org.example.bookstore.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.BookSearchParameters;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.example.bookstore.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toModel(bookDto);
        book.setCategories(categoriesIdToCategories(bookDto.getCategoryIds()));
        bookRepository.save(book);
        BookDto dto = bookMapper.toDto(book);
        setCategoryIds(dto, book);
        return dto;
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cannot find a book with id: " + id));
        BookDto dto = bookMapper.toDto(book);
        setCategoryIds(dto, book);
        return dto;
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.map(book -> {
            BookDto dto = bookMapper.toDto(book);
            setCategoryIds(dto, book);
            return dto;
        });
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Cannot find a book with id: " + id));
        book.setCategories(categoriesIdToCategories(bookDto.getCategoryIds()));
        bookMapper.updateBookFromDto(bookDto, book);
        bookRepository.save(book);
        BookDto dto = bookMapper.toDto(book);
        setCategoryIds(dto, book);
        return dto;
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParameters params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        Page<Book> bookPage = bookRepository.findAll(bookSpecification, pageable);
        return bookPage.map(book -> {
            BookDto dto = bookMapper.toDto(book);
            setCategoryIds(dto, book);
            return dto;
        });
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> getByCategoriesId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoriesId(id, pageable)
            .map(bookMapper::toDtoWithoutCategories);
    }

    private Set<Category> categoriesIdToCategories(List<Long> categories) {
        return categories.stream()
            .map(categoryRepository::getReferenceById)
            .collect(Collectors.toSet());
    }

    private void setCategoryIds(BookDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .toList()
        );
    }
}

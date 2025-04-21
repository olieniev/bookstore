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
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto getById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find a book with id: " + id)));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
            .map(bookMapper::toDto);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Cannot find a book with id: " + id));
        book.setCategories(categoriesIdToCategories(bookDto.getCategoryIds()));
        bookMapper.updateBookFromDto(bookDto, book);
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParameters params, Pageable pageable) {
        return bookRepository.findAll(bookSpecificationBuilder.build(params), pageable)
            .map(bookMapper::toDto);
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
}

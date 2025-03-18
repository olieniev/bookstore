package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookSearchParameters;
import org.example.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParameters params);
}

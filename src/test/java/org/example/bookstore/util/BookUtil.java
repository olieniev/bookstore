package org.example.bookstore.util;

import java.math.BigDecimal;
import java.util.List;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;

public class BookUtil {
    public static CreateBookRequestDto createBookRequestDto(Long categoryId) {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Test book");
        createBookRequestDto.setAuthor("Test author");
        createBookRequestDto.setIsbn("Isbn test");
        createBookRequestDto.setPrice(BigDecimal.valueOf(12.99));
        createBookRequestDto.setCategoryIds(List.of(categoryId));
        return createBookRequestDto;
    }

    public static Book createBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test book");
        book.setAuthor("Test author");
        book.setIsbn("Isbn test");
        book.setPrice(BigDecimal.valueOf(12.99));
        return book;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(3L);
        bookDto.setTitle("Test book");
        bookDto.setAuthor("Test author");
        bookDto.setIsbn("Isbn test");
        bookDto.setPrice(BigDecimal.valueOf(12.99));
        bookDto.setCategoryIds(List.of(1L));
        return bookDto;
    }

    public static BookDto mapBookDtoToBook(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategoryIds(List.of(1L));
        return bookDto;
    }

    public static Category createCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category name");
        return category;
    }

    public static List<BookDto> createListOfBookDtos() {
        BookDto firstDto = new BookDto();
        firstDto.setId(1L);
        firstDto.setTitle("first book");
        firstDto.setAuthor("first author");
        firstDto.setIsbn("first isbn");
        firstDto.setPrice(BigDecimal.valueOf(11));
        firstDto.setCategoryIds(List.of(1L));
        BookDto secondDto = new BookDto();
        secondDto.setId(2L);
        secondDto.setTitle("second book");
        secondDto.setAuthor("second author");
        secondDto.setIsbn("second isbn");
        secondDto.setPrice(BigDecimal.valueOf(12));
        secondDto.setCategoryIds(List.of(1L, 2L));
        BookDto thirdDto = new BookDto();
        thirdDto.setId(3L);
        thirdDto.setTitle("third book");
        thirdDto.setAuthor("third author");
        thirdDto.setIsbn("third isbn");
        thirdDto.setPrice(BigDecimal.valueOf(13));
        thirdDto.setCategoryIds(List.of(2L));
        return List.of(firstDto, secondDto, thirdDto);
    }
}

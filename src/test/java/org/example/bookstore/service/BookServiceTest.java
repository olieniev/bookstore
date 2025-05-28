package org.example.bookstore.service;

import static org.example.bookstore.util.BookUtil.createBook;
import static org.example.bookstore.util.BookUtil.createBookRequestDto;
import static org.example.bookstore.util.BookUtil.createCategory;
import static org.example.bookstore.util.BookUtil.mapBookDtoToBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.CategoryRepository;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.service.impl.BookServiceImpl;
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
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("""
        Get by id with existing id returns BookDto
            """)
    public void getBookById_correctId_returnsBookDto() {
        Long bookId = 1L;
        Book book = createBook(bookId);
        BookDto expected = mapBookDtoToBook(book);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.getById(bookId);
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("""
        Get by id with invalid id throws EntityNotFoundException
            """)
    public void getBookById_invalidId_throwsException() {
        Long bookId = 999L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(bookId));
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("""
        Save valid book returns BookDto
            """)
    public void save_validBook_returnsBookDto() {
        Long bookId = 1L;
        Long categoryId = 1L;
        CreateBookRequestDto requestDto = createBookRequestDto(categoryId);
        Book book = createBook(bookId);
        BookDto expected = mapBookDtoToBook(book);
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        Category mockCategory = createCategory();
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(mockCategory);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.save(requestDto);
        assertEquals(expected, actual);
        verify(bookMapper).toModel(requestDto);
        verify(categoryRepository).getReferenceById(categoryId);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("""
        Update valid book returns BookDto
            """)
    public void update_validBook_returnsBookDto() {
        Long bookId = 1L;
        Long categoryId = 2L;
        CreateBookRequestDto requestDto = createBookRequestDto(categoryId);
        Book book = createBook(bookId);
        BookDto expected = mapBookDtoToBook(book);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Category mockCategory = createCategory();
        when(categoryRepository.getReferenceById(categoryId)).thenReturn(mockCategory);
        doNothing().when(bookMapper).updateBookFromDto(requestDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);
        BookDto actual = bookService.update(bookId, requestDto);
        assertEquals(expected, actual);
        verify(bookRepository).findById(bookId);
        verify(categoryRepository).getReferenceById(categoryId);
        verify(bookMapper).updateBookFromDto(requestDto, book);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("""
        Update invalid book throws EntityNotFoundException
            """)
    public void update_inValidBook_throwsException() {
        Long bookId = 888L;
        Long categoryId = 1L;
        CreateBookRequestDto requestDto = createBookRequestDto(categoryId);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(bookId, requestDto));
        verify(bookRepository).findById(bookId);
    }

    @Test
    @DisplayName("""
        Delete book by id returns void
            """)
    public void delete_bookById_returnsVoid() {
        Long bookId = 888L;
        doNothing().when(bookRepository).deleteById(bookId);
        bookService.delete(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    @DisplayName("""
        Find all books returns page of BookDto
            """)
    public void find_allBooks_returnsBookDtos() {
        Pageable pageable = PageRequest.of(0, 1);
        Book book = createBook(1L);
        BookDto bookDto = mapBookDtoToBook(book);
        Page<Book> pageOfBooks = new PageImpl<>(List.of(book), pageable, 1);
        Page<BookDto> expected = new PageImpl<>(List.of(bookDto), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(pageOfBooks);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        Page<BookDto> actual = bookService.findAll(pageable);
        assertEquals(expected, actual);
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }
}

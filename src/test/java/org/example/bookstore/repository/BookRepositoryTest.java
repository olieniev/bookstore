package org.example.bookstore.repository;

import java.util.List;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
        Find books by category id returns books with request category id
            """)
    @Sql(scripts = {
        "classpath:database/categories/insert-two-categories.sql",
        "classpath:database/books/insert-three-books.sql",
        "classpath:database/bookscategories/insert-three-books-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
        "classpath:database/bookscategories/delete-from-books-categories.sql",
        "classpath:database/books/delete-from-books.sql",
        "classpath:database/categories/delete-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void find_AllBooksByCategoryId_returnsCorrectBooks() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> booksPage = bookRepository.findAllByCategoriesId(2L, pageable);
        List<String> actual = booksPage.map(Book::getTitle).toList();
        List<String> expected = List.of("second book", "third book");
        Assertions.assertEquals(expected, actual);
    }

}

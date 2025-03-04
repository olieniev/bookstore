package org.example.bookstore;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.model.Book;
import org.example.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class BookstoreApplication {
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setTitle("Catcher in the rye");
                book.setAuthor("J.D. Salinger");
                book.setIsbn("aaabbbccc");
                book.setPrice(BigDecimal.valueOf(100L));

                bookService.save(book);
                System.out.println(bookService.findAll());
            }
        };
    }
}

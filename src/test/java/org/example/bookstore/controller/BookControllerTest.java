package org.example.bookstore.controller;

import static org.example.bookstore.util.BookUtil.createBookDto;
import static org.example.bookstore.util.BookUtil.createBookRequestDto;
import static org.example.bookstore.util.BookUtil.createListOfBookDtos;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .apply(springSecurity())
            .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Create a new book with valid RequestDto is successful
            """)
    @Sql(scripts = "classpath:database/categories/insert-two-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
        "classpath:database/bookscategories/delete-from-books-categories.sql",
        "classpath:database/books/delete-from-books.sql",
        "classpath:database/categories/delete-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_validRequestDto_successful() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto(1L);
        BookDto expected = createBookDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Create a new book with invalid RequestDto throws Exception
            """)
    void createBook_inValidRequestDto_throwsException() throws Exception {
        CreateBookRequestDto requestDto = createBookRequestDto(1L);
        requestDto.setTitle("");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                post("/books")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("""
        Get all books method shows page of all books
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
    void getAll_booksExistInDb_successful() throws Exception {
        List<BookDto> expected = createListOfBookDtos();
        MvcResult result = mockMvc.perform(
                get("/books")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<BookDto> actual = objectMapper.readValue(
                contentNode.toString(),
            new TypeReference<List<BookDto>>() {}
        );
        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Update book method returns BookDto with updated data when the book exists
            """)
    @Sql(scripts = {
        "classpath:database/categories/insert-two-categories.sql",
        "classpath:database/categories/insert-single-category.sql",
        "classpath:database/books/insert-three-books.sql",
        "classpath:database/books/insert-single-book.sql",
        "classpath:database/bookscategories/insert-three-books-categories.sql",
        "classpath:database/bookscategories/insert-single-books-category.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
        "classpath:database/bookscategories/delete-from-books-categories.sql",
        "classpath:database/books/delete-from-books.sql",
        "classpath:database/categories/delete-from-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_bookExistInDb_successful() throws Exception {
        Long bookId = 4L;
        Long categoryId = 3L;
        CreateBookRequestDto requestDto = createBookRequestDto(categoryId);
        BookDto expected = createBookDto();
        expected.setId(bookId);
        expected.setCategoryIds(List.of(categoryId));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                put("/books/4")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Update book for non existing book id throws exception
            """)
    void updateBook_bookNotExistInDb_throwsException() throws Exception {
        Long categoryId = 2L;
        CreateBookRequestDto requestDto = createBookRequestDto(categoryId);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                put("/books/999")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNotFound())
                .andExpect(result ->
                Assertions.assertTrue(result.getResolvedException()
                    instanceof EntityNotFoundException)
            )
                .andExpect(result -> Assertions.assertEquals(
                "Cannot find a book with id: 999", result.getResolvedException().getMessage()));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Deleting an existing book is successful
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
    void deleteBook_bookExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Deleting an inexisting book is successful
            """)
    void deleteBook_bookNotExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/books/888")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }
}

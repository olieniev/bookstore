package org.example.bookstore.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.example.bookstore.util.CategoryUtil.createCategoryDto;
import static org.example.bookstore.util.CategoryUtil.createCategoryRequestDto;
import static org.example.bookstore.util.CategoryUtil.createListOfCategoryDtos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import org.example.bookstore.dto.category.CategoryDto;
import org.example.bookstore.dto.category.CategoryRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
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
public class CategoryControllerTest {
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
        Create a new category with valid RequestDto is successful
            """)
    @Sql(scripts = "classpath:database/categories/delete-from-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

    void createCategory_validRequestDto_successful() throws Exception {
        CategoryRequestDto requestDto = createCategoryRequestDto();
        CategoryDto expected = createCategoryDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                post("/categories")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Create a new category with invalid RequestDto throws Exception
            """)
    void createCategory_inValidRequestDto_throwsException() throws Exception {
        CategoryRequestDto requestDto = createCategoryRequestDto();
        requestDto.setName("");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                post("/categories")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("""
        Get all categories method shows page of all categories for user
            """)
    @Sql(scripts = "classpath:database/categories/insert-two-categories.sql")
    @Sql(scripts = "classpath:database/bookscategories/delete-from-books-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_categoriesExistInDb_successful() throws Exception {
        List<CategoryDto> expected = createListOfCategoryDtos();
        MvcResult result = mockMvc.perform(
                get("/categories")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        JsonNode contentNode = root.get("content");
        List<CategoryDto> actual = objectMapper.readValue(
                contentNode.toString(),
            new TypeReference<List<CategoryDto>>() {}
        );
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Update category method returns CategoryDto with updated data when the category exists
            """)
    @Sql(scripts = {
        "classpath:database/categories/insert-two-categories.sql",
        "classpath:database/categories/insert-single-category.sql",
    })
    @Sql(scripts = "classpath:database/categories/delete-from-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ExistInDb_successful() throws Exception {
        CategoryRequestDto requestDto = createCategoryRequestDto();
        requestDto.setName("changed name");
        CategoryDto expected = createCategoryDto();
        expected.setName("changed name");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(
                put("/categories/3")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );
        assertNotNull(actual);
        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Update category for non existing category id throws exception
            """)
    void updateCategory_CategoryNotExistInDb_throwsException() throws Exception {
        CategoryRequestDto requestDto = createCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(
                put("/categories/999")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNotFound())
                .andExpect(result ->
                assertTrue(result.getResolvedException()
                    instanceof EntityNotFoundException)
            )
                .andExpect(result -> assertEquals(
                "Can't find category by id: 999", result.getResolvedException().getMessage()));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Deleting an existing category is successful
            """)
    @Sql(scripts = {
        "classpath:database/categories/delete-from-categories.sql",
        "classpath:database/categories/insert-two-categories.sql"
    })
    @Sql(scripts = "classpath:database/categories/delete-from-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_CategoryExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/categories/1")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("""
        Deleting an inexisting category is successful
            """)
    void deleteCategory_categoryNotExistsInDb_successful() throws Exception {
        mockMvc.perform(
                delete("/categories/888")
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isNoContent());
    }
}

package org.example.bookstore.mapper;

import java.util.List;
import java.util.Set;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.book.BookDto;
import org.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.book.CreateBookRequestDto;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", source = "categories")
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(CreateBookRequestDto bookDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default List<Long> mapCategoryIds(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
            .map(Category::getId)
            .toList();
    }
}

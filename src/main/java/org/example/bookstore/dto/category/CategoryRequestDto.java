package org.example.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 4, max = 64)
    private String name;
    @Size(min = 8, max = 1000)
    private String description;
}

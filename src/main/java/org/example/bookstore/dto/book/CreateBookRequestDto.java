package org.example.bookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9][A-Za-z0-9 ,.'\"-;:!?()&]*[A-Za-z0-9]$\n")
    private String title;
    @NotBlank
    @Pattern(regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$")
    private String author;
    @NotBlank
    private String isbn;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    private String description;
    private String coverImage;
}

package org.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddBookToCartDto(
        @NotNull
        @Positive
        Long id,
        @Positive
        int quantity
) {
}

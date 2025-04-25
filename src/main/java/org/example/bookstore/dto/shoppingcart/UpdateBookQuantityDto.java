package org.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record UpdateBookQuantityDto(
        @Positive
        int quantity
) {
}

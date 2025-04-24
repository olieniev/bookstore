package org.example.bookstore.dto.shoppingcart;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateBookQuantityDto(
        @NotNull
        @Min(1)
        @Max(100)
        int quantity
) {
}
